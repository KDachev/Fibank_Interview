package com.interview.service;

import com.interview.model.BalanceRequest;
import com.interview.model.BalanceResponse;
import com.interview.model.CashOperation;

import java.io.IOException;
import java.util.List;

public interface CashierService {

    void withdraw(CashOperation cashOperation) throws IOException;
    void deposit(CashOperation cashOperation) throws IOException;
    List<BalanceResponse> getFilteredBalance(BalanceRequest balanceRequest) throws IOException;
}
