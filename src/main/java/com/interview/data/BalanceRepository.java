package com.interview.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.model.Balance;
import com.interview.model.Currency;
import com.interview.model.Denomination;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;

@Repository
public class BalanceRepository {
    private static final String BALANCES_TXT = "Balances.txt";
    private ObjectMapper mapper;

    public BalanceRepository() throws IOException {
        mapper = new ObjectMapper();
        init();
    }

    private void init() throws IOException {
        File file = new File(BALANCES_TXT);
        if (file.delete());
            // log
        if (file.createNewFile());
            //log;

        Map<Currency, Map<Denomination, Integer>> balance = new HashMap<>();
        balance.put(Currency.BGN, new HashMap<>());
        balance.get(Currency.BGN).put(Denomination.FIFTY, 10);
        balance.get(Currency.BGN).put(Denomination.TWENTY, 0);
        balance.get(Currency.BGN).put(Denomination.TEN, 50);
        balance.put(Currency.EUR, new HashMap<>());
        balance.get(Currency.EUR).put(Denomination.FIFTY, 20);
        balance.get(Currency.EUR).put(Denomination.TWENTY, 0);
        balance.get(Currency.EUR).put(Denomination.TEN, 100);
        addBalance(new Balance(1, 1, balance));
        addBalance(new Balance(2, 2, balance));
        addBalance(new Balance(3, 3, balance));
    }

    public void addBalance(Balance balance) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BALANCES_TXT, true))) {
            String json = mapper.writeValueAsString(balance);
            writer.write(json);
            //log
            writer.newLine();
        }
    }

    /**
     * Contacts database to retrieve all balances
     * @return a list of all balances
     * @throws IOException
     */
    public List<Balance> getAllBalances() throws IOException {
        List<Balance> balances = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(BALANCES_TXT))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Balance op = mapper.readValue(line, Balance.class);
                balances.add(op);
            }
        }

        return balances;
    }

    /**
     * Contacts the database to retrieve all balances for specific cashier
     * @param cashierId - id of the Cashier
     * @return a list of balances for that cashier
     * @throws IOException
     */
    public List<Balance> getAllBalanceForCashier(long cashierId) throws IOException {
        List<Balance> balances = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(BALANCES_TXT))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Balance balance = mapper.readValue(line, Balance.class);
                if (balance.getCashierId() == cashierId)
                    balances.add(balance);
            }
        }

        return balances;
    }

    public Optional<Balance> findById(long balanceId) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(BALANCES_TXT))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Balance balance = mapper.readValue(line, Balance.class);
                if (balance.getBalanceId() == balanceId)
                    return Optional.of(balance);
            }
        }

        return Optional.empty();
    }

    public Balance updateBalance(long balanceId, Currency currency, Map<Denomination, Integer> newBalance) throws IOException {
        Balance balance = new Balance();
        boolean balanceFound = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(BALANCES_TXT))) {
            String line;
            while ((line = reader.readLine()) != null) {
                balance = mapper.readValue(line, Balance.class);

                if (balance.getBalanceId() == balanceId) {
                    balance.getDenominations().put(currency, newBalance);
                    balanceFound = true;
                    break;
                }
            }
        }
        if (!balanceFound)
            throw new IllegalArgumentException("Couldn't find balance with id " + balanceId);

        deleteBalanceById(balanceId);
        addBalance(balance);
        return balance;
    }

    public boolean isBalanceOwnedByCashier(long cashierId, long balanceId) throws IOException{
        try (BufferedReader reader = new BufferedReader(new FileReader(BALANCES_TXT))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Balance balance = mapper.readValue(line, Balance.class);
                if (balance.getBalanceId() == balanceId && balance.getCashierId() == cashierId)
                    return true;
            }
        }

        return false;
    }

    private void deleteBalanceById(long balanceIdToDelete) throws IOException {
        File inputFile = new File(BALANCES_TXT);
        File tempFile = new File("balances_temp.txt");

        try (
                BufferedReader reader = new BufferedReader(new FileReader(BALANCES_TXT));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                Balance balance = mapper.readValue(line, Balance.class);
                if (balance.getBalanceId() != balanceIdToDelete) {
                    writer.write(mapper.writeValueAsString(balance));
                    writer.newLine();
                }
            }
        }

        // Replace original file with the updated one
        if (!inputFile.delete()) {
            throw new IOException("Could not delete original file");
        }
        if (!tempFile.renameTo(inputFile)) {
            throw new IOException("Could not rename temp file");
        }
    }
}
