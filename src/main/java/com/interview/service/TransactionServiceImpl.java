package com.interview.service;

import com.interview.data.TransactionRepository;
import com.interview.model.TransactionRecord;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;


    @Override
    public void record(TransactionRecord transactionRecord) throws IOException {
        transactionRepository.record(transactionRecord);
    }

    @Override
    public List<TransactionRecord> getAllTransactions() throws IOException {
        return transactionRepository.getAllTransactions();
    }

    public Map<Long, List<TransactionRecord>> getTransactionsPerBalance() throws IOException {
        return transactionRepository.getTransactionsPerBalance();
    }
}
