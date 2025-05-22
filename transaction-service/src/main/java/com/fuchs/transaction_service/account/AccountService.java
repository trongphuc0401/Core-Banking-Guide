package com.fuchs.transaction_service.account;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final RestTemplate restTemplate;
    private final String customerServiceUrl;

    public AccountService(AccountRepository accountRepository, RestTemplate restTemplate, @Value("${customer-service.url}") String customerServiceUrl) {
        this.accountRepository = accountRepository;
        this.restTemplate = restTemplate;
        this.customerServiceUrl = customerServiceUrl;
    }

    public Account createAccount(Long customerId, BigDecimal initialBalance, String accountNumber) {
        // Validate customer exists by calling Customer Management Service
        String url = customerServiceUrl + "/" + customerId;
        try {
            restTemplate.getForObject(url, Object.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid customer ID: " + customerId);
        }

        // Validate inputs
        if (initialBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }
        if (accountNumber == null || accountNumber.isEmpty()) {
            throw new IllegalArgumentException("Account number is required");
        }

        Account account = new Account(customerId, initialBalance, accountNumber);
        return accountRepository.save(account);
    }
}