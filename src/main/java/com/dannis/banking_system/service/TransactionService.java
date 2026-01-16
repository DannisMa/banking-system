package com.dannis.banking_system.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dannis.banking_system.model.Transaction;
import com.dannis.banking_system.repository.TransactionRepository;

@Service
public class TransactionService 
{
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }
    
    public void recordTransaction(Long fromAccountId, Long toAccountId, java.math.BigDecimal amount)
    {
        
        Transaction record = new Transaction(fromAccountId, toAccountId, amount, java.time.LocalDateTime.now());
        transactionRepository.save(record);
        System.out.println("交易紀錄已保存");
    }

    public List<Transaction> getTransactionsForAccount(Long accountId) {
        return transactionRepository.findByFromAccountIdOrToAccountIdOrderByTimestampDesc(accountId, accountId);
    }
}
