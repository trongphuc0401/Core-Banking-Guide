package com.fuchs.transaction_service.transaction;


import com.fuchs.transaction_service.account.Account;
import com.fuchs.transaction_service.account.AccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
        private final KafkaTemplate<String, String> kafkaTemplate;

    public TransactionService(AccountRepository accountRepository, TransactionRepository transactionRepository,
                              KafkaTemplate<String, String> kafkaTemplate) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional
    public Transaction deposit(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + accountId));

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }

        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);

        Transaction transaction = new Transaction(account, TransactionType.CASH_IN, amount, LocalDateTime.now());
        transaction = transactionRepository.save(transaction);

        // Publish event to Kafka
        kafkaTemplate.send("transactions", "Deposit completed for account: " + accountId);

        return transaction;
    }

    @Transactional
    public Transaction withdraw(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + accountId));

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        if (account.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);

        Transaction transaction = new Transaction(account, TransactionType.CASH_OUT, amount, LocalDateTime.now());
        transaction = transactionRepository.save(transaction);

        // Publish event to Kafka
        kafkaTemplate.send("transactions", "Withdrawal completed for account: " + accountId);

        return transaction;
    }
}
