package com.interview.config;

import com.interview.data.BalanceRepository;
import com.interview.service.BalanceService;
import com.interview.service.BalanceServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class BalanceConfig {

    @Bean
    public BalanceRepository getBalanceRepository() throws IOException {
        return new BalanceRepository();
    }

    @Bean
    public BalanceService getBalanceService()  throws IOException {
        return new BalanceServiceImpl();
    }
}
