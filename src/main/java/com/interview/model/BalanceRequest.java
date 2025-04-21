package com.interview.model;

import java.time.LocalDate;

public class BalanceRequest {
    private String cashierName;
    private LocalDate dateFrom;
    private LocalDate dateTo;

    public String getCashierName() {
        return cashierName;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }
}
