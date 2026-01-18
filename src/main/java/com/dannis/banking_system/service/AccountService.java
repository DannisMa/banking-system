package com.dannis.banking_system.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.dannis.banking_system.model.Account;
import com.dannis.banking_system.repository.AccountRepository;

import jakarta.transaction.Transactional;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final TransactionService transactionService;

    public AccountService(AccountRepository accountRepository, TransactionService transactionService) {
        this.accountRepository = accountRepository;
        this.transactionService = transactionService;
    }

    @Transactional
    public void transfer(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        if(amount.compareTo(BigDecimal.ZERO) <= 0)
        {
            throw new RuntimeException("轉帳金額必大於0");
        }
        Long firstLockId = fromAccountId < toAccountId ? fromAccountId : toAccountId;
        Long secondLockId = fromAccountId < toAccountId ? toAccountId : fromAccountId;

        Account firstAccount = accountRepository.findByIdForUpdate(firstLockId).orElseThrow(()->new RuntimeException(firstLockId + "帳戶不存在"));
        Account secondAccount = accountRepository.findByIdForUpdate(secondLockId).orElseThrow(()->new RuntimeException(secondLockId + "帳戶不存在"));

        Account fromAccount = (firstAccount.getId().equals(fromAccountId)) ? firstAccount : secondAccount;
        Account toAccount = (firstAccount.getId().equals(toAccountId)) ? firstAccount : secondAccount;

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
