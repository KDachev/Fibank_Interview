package com.interview.model;

import java.util.HashMap;
import java.util.Map;

public class Balance {
    private long balanceId;
    private long cashierId;
    private Map<Currency, Integer> currencyTotal;
    private Map<Currency, Map<Denomination, Integer>> denominations;

    public Balance() {}

    public Balance(long balanceId, long cashierId, Map<Currency, Map<Denomination, Integer>> denominations) {
        this.balanceId = balanceId;
        this.cashierId = cashierId;
        this.denominations = denominations;
    }

    public Balance(long balanceId, long cashierId, Map<Currency, Integer> currencyTotal, Map<Currency, Map<Denomination, Integer>> denominations) {
        this.balanceId = balanceId;
        this.cashierId = cashierId;
        this.currencyTotal = currencyTotal;
        this.denominations = denominations;
    }

    public long getBalanceId() {
        return balanceId;
    }

    public long getCashierId() {
        return cashierId;
    }

    public Map<Currency, Map<Denomination, Integer>> getDenominations() {
        return denominations;
    }

    public Map<Currency, Integer> getCurrencyTotal() {
        Map<Currency, Integer> totalForCurrencies = new HashMap<>();
        for (Currency currency : Currency.values()) {
            int currentTotal = 0;
            for (Map.Entry<Denomination, Integer> entry : denominations.get(currency).entrySet())
                currentTotal += entry.getKey().getValue() * entry.getValue();

            totalForCurrencies.put(currency, currentTotal);
        }

        return totalForCurrencies;
    }
}
