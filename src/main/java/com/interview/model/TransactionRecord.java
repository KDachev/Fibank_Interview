package com.interview.model;

import java.time.LocalDate;
import java.util.Map;

public class TransactionRecord {
    private long cashierId;
    private long balanceId;
    private LocalDate date;
    private Transaction transaction;
    private Currency currency;
    private Map<Denomination, Integer> sum;
    private Map<Currency, Map<Denomination, Integer>> updatedBalance;

    public TransactionRecord() {}

    public TransactionRecord(CashOperation cashOperation, Map<Currency, Map<Denomination, Integer>> updatedBalance) {
        cashierId = cashOperation.getCashierId();
        balanceId = cashOperation.getBalanceId();
        date = cashOperation.getDate();
        transaction = cashOperation.getTransaction();
        currency = cashOperation.getCurrency();
        sum = cashOperation.getSum();
        this.updatedBalance = updatedBalance;
    }

    public long getCashierId() {
        return cashierId;
    }

    public long getBalanceId() {
        return balanceId;
    }

    public LocalDate getDate() {
        return date;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Map<Denomination, Integer> getSum() {
        return sum;
    }

    public Map<Currency, Map<Denomination, Integer>> getUpdatedBalance() {
        return updatedBalance;
    }
}
