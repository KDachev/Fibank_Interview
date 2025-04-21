package com.interview.config;

import com.interview.data.TransactionRepository;
import com.interview.service.TransactionService;
import com.interview.service.TransactionServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class TransactionConfig {

    @Bean
    public TransactionRepository getTransactionRepository() throws IOException {
        return new TransactionRepository();
    }

    @Bean
    public TransactionService getTransactionService() {
        return new TransactionServiceImpl();
    }
}
