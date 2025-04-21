package com.interview.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.model.Cashier;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.List;
import java.util.Optional;

@Repository
public class CashierRepository {
    private static final String CASHIER_TXT = "Cashier.txt";
    private ObjectMapper mapper;

    public CashierRepository() throws IOException {
        mapper = new ObjectMapper();
        init();
    }

    private void init() throws IOException {
        File file = new File(CASHIER_TXT);
        if (file.delete()) ;
        // log
        if (file.createNewFile()) ;
        //log;

        addCashier(new Cashier(1, "MARTINA", List.of(1L)));
        addCashier(new Cashier(2, "PETER", List.of(2L)));
        addCashier(new Cashier(3, "LINDA", List.of(3L)));
    }

    public void addCashier(Cashier cashier) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CASHIER_TXT, true))) {
            String json = mapper.writeValueAsString(cashier);
            writer.write(json);
            writer.newLine();
        }
    }

    public Optional<Cashier> findById(long cashierId) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(CASHIER_TXT))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Cashier cashier = mapper.readValue(line, Cashier.class);
                if (cashier.getId() == cashierId)
                    return Optional.of(cashier);
            }
        }

        return Optional.empty();
    }

    public Optional<Cashier> findByName(String name) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(CASHIER_TXT))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Cashier cashier = mapper.readValue(line, Cashier.class);
                if (cashier.getName().equals(name))
                    return Optional.of(cashier);
            }
        }

        return Optional.empty();
    }
}
