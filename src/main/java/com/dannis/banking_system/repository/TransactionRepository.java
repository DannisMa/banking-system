package com.dannis.banking_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dannis.banking_system.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByFromAccountIdOrToAccountIdOrderByTimestampDesc(Long fromAccountId, Long toAccountId);
} 