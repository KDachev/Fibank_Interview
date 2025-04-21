package com.interview.controller;

import com.interview.model.*;
import com.interview.service.BalanceService;
import com.interview.service.CashierService;
import com.interview.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/v1")
public class CashOperationsController {
    @Autowired
    private BalanceService balanceService;
    @Autowired
    private CashierService cashierService;
    @Autowired
    private TransactionService transactionService;

    @PutMapping("cash-operation")
    public void cashOperation(@RequestBody CashOperation cashOperation) throws IOException {
        if (cashOperation.getTransaction() == Transaction.WITHDRAWAL) {
            cashierService.withdraw(cashOperation);
        } else {
            cashierService.deposit(cashOperation);
        }
    }

    @GetMapping("cash-balance")
    public List<BalanceResponse> getBalance(@RequestBody(required = false) BalanceRequest balanceRequest) throws IOException {
        return cashierService.getFilteredBalance(balanceRequest);
    }
}
