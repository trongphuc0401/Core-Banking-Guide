package com.fuchs.transaction_service.account;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "account", schema = "banking")
public class Account {

    // Getters and Setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId; // Links to Customer in Customer Management Service
    private BigDecimal balance;
    private String accountNumber;

    public Account() {}

    public Account(Long customerId, BigDecimal balance, String accountNumber) {
        this.customerId = customerId;
        this.balance = balance;
        this.accountNumber = accountNumber;
    }

}
