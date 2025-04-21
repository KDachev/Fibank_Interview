package com.interview.service;

import com.interview.model.TransactionRecord;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface TransactionService {

    void record(TransactionRecord transactionRecord) throws IOException;

    List<TransactionRecord> getAllTransactions() throws IOException;

    Map<Long, List<TransactionRecord>> getTransactionsPerBalance() throws IOException;
}
