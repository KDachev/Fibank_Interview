package com.interview.service;

import com.interview.data.CashierRepository;
import com.interview.model.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

public class CashierServiceImpl implements CashierService {
    @Autowired
    private CashierRepository cashierRepository;
    @Autowired
    private BalanceService balanceService;
    @Autowired
    private TransactionService transactionService;

    public void withdraw(CashOperation cashOperation) throws IOException {
        if (balanceService.isBalanceOwnedByCashier(cashOperation.getCashierId(), cashOperation.getBalanceId())) {
            Balance updatedBalance = balanceService.withdraw(
                    cashOperation.getBalanceId(), cashOperation.getCurrency(), cashOperation.getSum());

            transactionService.record(new TransactionRecord(cashOperation, updatedBalance.getDenominations()));
        } else {
            throw new IllegalArgumentException ("Cashier doesn't own this balance.");
        }
    }

    public void deposit(CashOperation cashOperation) throws IOException {
        if (balanceService.isBalanceOwnedByCashier(cashOperation.getCashierId(), cashOperation.getBalanceId())) {
            Balance updatedBalance = balanceService.deposit(
                    cashOperation.getBalanceId(), cashOperation.getCurrency(), cashOperation.getSum());

            transactionService.record(new TransactionRecord(cashOperation, updatedBalance.getDenominations()));
        } else {
            throw new IllegalArgumentException ("Cashier doesn't own this balance.");
        }
    }

    public List<BalanceResponse> getFilteredBalance(BalanceRequest balanceRequest) throws IOException {
        long cashierId = -1;
        if (balanceRequest.getCashierName() != null) {
            cashierId = cashierRepository.findByName(balanceRequest.getCashierName())
                    .orElseThrow(() -> new IllegalArgumentException("No such Cashier exists."))
                    .getId();
        }
        return balanceService.getFilteredBalances(cashierId, balanceRequest.getDateFrom(), balanceRequest.getDateTo());
    }
}
