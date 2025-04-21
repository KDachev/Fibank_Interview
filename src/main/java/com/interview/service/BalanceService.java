package com.interview.service;

import com.interview.model.Balance;
import com.interview.model.BalanceResponse;
import com.interview.model.Currency;
import com.interview.model.Denomination;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface BalanceService {

    Balance withdraw(long balanceId, Currency currency, Map<Denomination, Integer> withdrawMap) throws IOException;

    Balance deposit(long balanceId, Currency currency, Map<Denomination, Integer> depositMap) throws IOException;

    boolean isBalanceOwnedByCashier(long cashierId, long balanceId) throws IOException;

    List<Balance> getAllBalances() throws IOException;

    List<BalanceResponse> getFilteredBalances(long cashierId, LocalDate dateFrom, LocalDate dateTo) throws IOException;
}
