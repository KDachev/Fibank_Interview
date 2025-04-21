package com.interview.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.interview.model.TransactionRecord;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TransactionRepository {
    private static final String TRANSACTION_TXT = "TransactionHistory.txt";
    private ObjectMapper mapper;

    public TransactionRepository() throws IOException {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        init();
    }

    private void init() throws IOException {
        File file = new File(TRANSACTION_TXT);
        if (file.delete()) ;
        // log
        if (file.createNewFile()) ;
        //log;
    }

    public List<TransactionRecord> getAllTransactions() throws IOException {
        List<TransactionRecord> operations = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(TRANSACTION_TXT))) {
            String line;
            while ((line = reader.readLine()) != null) {
                TransactionRecord record = mapper.readValue(line, TransactionRecord.class);
                operations.add(record);
            }
        }

        return operations;
    }

    public Map<Long, List<TransactionRecord>> getTransactionsPerBalance() throws IOException {
        Map<Long, List<TransactionRecord>> transactions = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(TRANSACTION_TXT))) {
            String line;
            while ((line = reader.readLine()) != null) {
                TransactionRecord record = mapper.readValue(line, TransactionRecord.class);
                transactions.computeIfAbsent(record.getBalanceId(), k -> new ArrayList<>());
                transactions.get(record.getBalanceId()).add(record);
            }
        }

        return transactions;
    }

    public void record(TransactionRecord transactionRecord) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TRANSACTION_TXT, true))) {
            String json = mapper.writeValueAsString(transactionRecord);
            writer.write(json);
            writer.newLine();
        }
    }
}
