package com.ahmed.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class TransactionConfig {
    // Transaction management is automatically configured by Spring Boot
    // This class enables transaction management for the application
}