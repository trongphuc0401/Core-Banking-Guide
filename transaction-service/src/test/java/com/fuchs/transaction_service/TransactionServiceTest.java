package com.fuchs.transaction_service;

import com.fuchs.transaction_service.account.Account;
import com.fuchs.transaction_service.account.AccountRepository;
import com.fuchs.transaction_service.transaction.Transaction;
import com.fuchs.transaction_service.transaction.TransactionRepository;
import com.fuchs.transaction_service.transaction.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TransactionServiceTest {

	@Autowired
	private TransactionService transactionService;

	@MockitoBean
	private AccountRepository accountRepository;

	@MockitoBean
	private TransactionRepository transactionRepository;

	@MockitoBean
	private KafkaTemplate<String, String> kafkaTemplate;

	@Test
	public void testDeposit() {
		Account account = new Account(1L, BigDecimal.valueOf(100), "ACC123");
		when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
		when(accountRepository.save(any(Account.class))).thenReturn(account);
		when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArgument(0));

		Transaction transaction = transactionService.deposit(1L, BigDecimal.valueOf(50));

		assertEquals("DEPOSIT", transaction.getType());
		assertEquals(BigDecimal.valueOf(50), transaction.getAmount());
		assertEquals(BigDecimal.valueOf(150), account.getBalance());
	}
}