package com.dannis.banking_system.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.dannis.banking_system.model.Account;
import com.dannis.banking_system.repository.AccountRepository;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final TransactionService transactionService;

    public AccountService(AccountRepository accountRepository, TransactionService transactionService) {
        this.accountRepository = accountRepository;
        this.transactionService = transactionService;
    }

    public void transfer(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        if(amount.compareTo(BigDecimal.ZERO) <= 0)
        {
            throw new RuntimeException("轉帳金額必大於0");
        }
        Account fromAccount = accountRepository.findById(fromAccountId).orElseThrow(() -> new RuntimeException("找不到轉出帳戶 ID: " + fromAccountId));
        Account toAccount = accountRepository.findById(toAccountId).orElseThrow(()-> new RuntimeException("找不到轉入帳戶 ID: " + toAccountId));

        if(fromAccount.getBalance().compareTo(amount) < 0)
        {
            throw new RuntimeException("轉出帳戶餘額不足");
        }

        BigDecimal newFromBalance = fromAccount.getBalance().subtract(amount);
        fromAccount.setBalance(newFromBalance);
        BigDecimal newToBalance = toAccount.getBalance().add(amount);
        toAccount.setBalance(newToBalance);

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        System.out.println("成功從帳戶 " + fromAccountId + " 轉帳 " + amount + " 到帳戶 " + toAccountId);
        transactionService.recordTransaction(fromAccountId, toAccountId, amount);

    }

    public Account getAccountById(Long accountId) {
        return accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("找不到帳戶 ID: " + accountId));
    }
}
