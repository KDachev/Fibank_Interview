package com.interview.model;

import java.time.LocalDate;
import java.util.Map;

public class CashOperation {
    private long cashierId;
    private long balanceId;
    private LocalDate date;
    private Transaction transaction;
    private Currency currency;
    private Map<Denomination, Integer> sum;

    public long getCashierId() {
        return cashierId;
    }

    public long getBalanceId() {
        return balanceId;
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

    public LocalDate getDate() {
        return date;
    }
}
