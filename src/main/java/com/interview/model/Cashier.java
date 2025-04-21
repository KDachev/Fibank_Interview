package com.interview.model;

import java.util.List;

public class Cashier {
    private long id;
    private String name;
    private List<Long> balanceIds;

    public Cashier() {}

    public Cashier(long id, String name, List<Long> balanceIds) {
        this.id = id;
        this.name = name;
        this.balanceIds = balanceIds;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Long> getBalanceIds() {
        return balanceIds;
    }
}
