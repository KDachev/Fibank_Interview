package com.interview.model;

import java.util.List;
import java.util.Map;

public class BalanceResponse {
    private long balanceId;
    private long cashierId;
    private Map<Currency, Integer> currencyTotal;
    private Map<Currency, Map<Denomination, Integer>> denominations;
    private List<TransactionRecord> transactionRecords;
    private Map<Currency, Integer> balanceChanges;

    public BalanceResponse(Balance balance) {
        balanceId = balance.getBalanceId();
        cashierId = balance.getCashierId();
        currencyTotal = balance.getCurrencyTotal();
        denominations = balance.getDenominations();
    }

    public BalanceResponse(long balanceId, long cashierId, Map<Currency, Integer> currencyTotal,
                           Map<Currency, Map<Denomination, Integer>> denominations,
                           List<TransactionRecord> transactionRecords, Map<Currency, Integer> balanceChanges)
    {
        this.balanceId = balanceId;
        this.cashierId = cashierId;
        this.currencyTotal = currencyTotal;
        this.denominations = denominations;
        this.transactionRecords = transactionRecords;
        this.balanceChanges = balanceChanges;
    }

    public long getBalanceId() {
        return balanceId;
    }

    public long getCashierId() {
        return cashierId;
    }

    public Map<Currency, Integer> getCurrencyTotal() {
        return currencyTotal;
    }

    public Map<Currency, Map<Denomination, Integer>> getDenominations() {
        return denominations;
    }

    public List<TransactionRecord> getTransactionRecords() {
        return transactionRecords;
    }

    public Map<Currency, Integer> getBalanceChanges() {
        return balanceChanges;
    }
}
