package com.interview.config;

import com.interview.data.CashierRepository;
import com.interview.service.CashierService;
import com.interview.service.CashierServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class CashierConfig {

    @Bean
    public CashierService getCashierService() {
        return new CashierServiceImpl();
    }

    @Bean
    public CashierRepository getCashierRepository() throws IOException {
        return new CashierRepository();
    }
}
