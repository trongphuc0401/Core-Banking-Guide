package com.fuchs.transaction_service.transaction;


import com.fuchs.transaction_service.account.Account;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@Table(name = "transaction", schema = "banking")
public class Transaction {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private TransactionType type; // CASH-IN, CASH-OUT, DEBIT, PAYMENT, TRANSFER.
    private BigDecimal amount;
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    public Transaction() {}

    public Transaction(Account accountId, TransactionType type, BigDecimal amount, LocalDateTime timestamp) {
        this.account = accountId;
        this.type = type;
        this.amount = amount;
        this.timestamp = timestamp;
    }

}
