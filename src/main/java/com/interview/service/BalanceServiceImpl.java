package com.interview.service;

import com.interview.data.BalanceRepository;
import com.interview.model.*;
import com.interview.model.Currency;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class BalanceServiceImpl implements BalanceService {
    @Autowired
    private BalanceRepository balanceRepository;
    @Autowired
    private TransactionService transactionService;

    @Override
    public Balance withdraw(long balanceId, Currency currency, Map<Denomination, Integer> withdrawMap) throws IOException {
        Map<Denomination, Integer> currentDenominations = getBalanceById(balanceId).getDenominations().get(currency);

        for (Map.Entry<Denomination, Integer> entry : withdrawMap.entrySet()) {
            Denomination denom = entry.getKey();
            int requestedCount = entry.getValue();
            int availableCount = currentDenominations.getOrDefault(denom, 0);

            if (requestedCount < 0)
                throw new IllegalArgumentException("Cannot withdraw a negative number of bills.");

            if (availableCount < requestedCount)
                throw new IllegalArgumentException("Not enough " + denom.getValue() + " " + currency + " bills. Requested: "
                        + requestedCount + ", Available: " + availableCount);
        }

        for (Map.Entry<Denomination, Integer> entry : withdrawMap.entrySet()) {
            Denomination denomination = entry.getKey();
            int requestedCount = entry.getValue();
            currentDenominations.compute(denomination, (k, currentCount) -> currentCount - requestedCount);
        }

        return balanceRepository.updateBalance(balanceId, currency, currentDenominations);
    }

    @Override
    public Balance deposit(long balanceId, Currency currency, Map<Denomination, Integer> depositMap) throws IOException {
        Map<Denomination, Integer> currentDenominations = getBalanceById(balanceId).getDenominations().get(currency);

        for (Map.Entry<Denomination, Integer> entry : depositMap.entrySet()) {
            Denomination denomination = entry.getKey();
            int requestedCount = entry.getValue();
            currentDenominations.compute(denomination, (k, currentCount) -> currentCount + requestedCount);
        }

        return balanceRepository.updateBalance(balanceId, currency, currentDenominations);
    }

    @Override
    public boolean isBalanceOwnedByCashier(long cashierId, long balanceId) throws IOException{
        return balanceRepository.isBalanceOwnedByCashier(cashierId, balanceId);
    }

    @Override
    public List<Balance> getAllBalances() throws IOException {
        return balanceRepository.getAllBalances();
    }

    @Override
    public List<BalanceResponse> getFilteredBalances(long cashierId, LocalDate dateFrom, LocalDate dateTo) throws IOException {
        Map<Long, List<TransactionRecord>> transactions = transactionService.getTransactionsPerBalance();
        if (dateFrom != null || dateTo != null) {
            if (cashierId != -1) {
                transactions = transactions.entrySet().stream()
                        .map(entry -> Map.entry(
                                entry.getKey(),
                                entry.getValue().stream()
                                        .filter(record -> record.getCashierId() == cashierId)
                                        .collect(Collectors.toList())))
                        .filter(entry -> !entry.getValue().isEmpty()) // Remove empty lists
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            }
            if (dateFrom != null) {
                transactions = transactions.entrySet().stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey, entry -> entry.getValue().stream()
                                        .filter(record -> (record.getDate().plusDays(1)).isAfter(dateFrom))
                                        .collect(Collectors.toList())));
            }
            if (dateTo != null) {
                if (dateFrom != null) {
                    if (dateFrom.isAfter(dateTo)) {
                        throw new IllegalArgumentException("Date From cannot be after Date To.");
                    }
                }

                transactions = transactions.entrySet().stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey, entry -> entry.getValue().stream()
                                        .filter(record -> record.getDate().minusDays(1).isBefore(dateTo))
                                        .collect(Collectors.toList())));
            }
            List<BalanceResponse> balanceResponses = new ArrayList<>();
            if (!transactions.isEmpty()) {
                for (Map.Entry<Long, List<TransactionRecord>> entry : transactions.entrySet()) {
                    TransactionRecord firstTransaction = entry.getValue().get(0);
                    Balance initialBalance = new Balance(
                            firstTransaction.getBalanceId(), firstTransaction.getCashierId(), firstTransaction.getUpdatedBalance());
                    TransactionRecord lastTransaction = entry.getValue().get(entry.getValue().size() - 1);
                    Balance lastBalance = new Balance(
                            lastTransaction.getBalanceId(), lastTransaction.getCashierId(), lastTransaction.getUpdatedBalance());

                    Map<Currency, Integer> balanceChanges = new HashMap<>(initialBalance.getCurrencyTotal());

                    for (Map.Entry<Currency, Integer> initialBalanceEntry : balanceChanges.entrySet()) {
                        Currency key = initialBalanceEntry.getKey();
                        int initialTotalBalance = initialBalanceEntry.getValue();
                        int balanceBeforeFirstTransaction = 0;

                        // get the initial balance before the first transaction
                        for (Map.Entry<Denomination, Integer> firstTransactionSumEntry : firstTransaction.getSum().entrySet())
                            balanceBeforeFirstTransaction += firstTransactionSumEntry.getKey().getValue() * firstTransactionSumEntry.getValue();
                        if (firstTransaction.getTransaction() == Transaction.DEPOSIT)
                            balanceBeforeFirstTransaction = balanceBeforeFirstTransaction * -1;
                        if (firstTransaction.getCurrency() == key)
                            initialTotalBalance += balanceBeforeFirstTransaction;


                        int lastTotalBalance = lastBalance.getCurrencyTotal().getOrDefault(key, 0);
                        initialBalanceEntry.setValue(lastTotalBalance - initialTotalBalance);
                    }

                    BalanceResponse response = new BalanceResponse(firstTransaction.getBalanceId(),
                            firstTransaction.getCashierId(), lastBalance.getCurrencyTotal(), lastBalance.getDenominations(),
                            entry.getValue(), balanceChanges);
                    balanceResponses.add(response);
                }
            }
            return balanceResponses;
        }


        List<Balance> balances = balanceRepository.getAllBalances();
        if (cashierId != -1)
            balances = balances.stream()
                .filter(balance -> balance.getCashierId() == cashierId)
                .toList();

        return balances.stream().map(BalanceResponse::new).toList();
    }

    private Balance getBalanceById(long balanceId) throws IOException {
        return balanceRepository.findById(balanceId)
                .orElseThrow(() -> new IllegalArgumentException("Couldn't find a balance with this Id."));
    }
}
