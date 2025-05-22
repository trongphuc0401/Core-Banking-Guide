package com.fuchs.transaction_service.transaction;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/deposit")
    public ResponseEntity<Transaction> deposit(@RequestParam Long accountId, @RequestParam BigDecimal amount) {
        Transaction transaction = transactionService.deposit(accountId, amount);
        return ResponseEntity.ok(transaction);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Transaction> withdraw(@RequestParam Long accountId, @RequestParam BigDecimal amount) {
        Transaction transaction = transactionService.withdraw(accountId, amount);
        return ResponseEntity.ok(transaction);
    }

}
