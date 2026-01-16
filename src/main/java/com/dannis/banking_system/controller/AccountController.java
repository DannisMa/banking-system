package com.dannis.banking_system.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dannis.banking_system.dto.AccountResponse;
import com.dannis.banking_system.dto.TransferRequest;
import com.dannis.banking_system.model.Account;
import com.dannis.banking_system.model.Transaction;
import com.dannis.banking_system.repository.AccountRepository;
import com.dannis.banking_system.repository.TransactionRepository;
import com.dannis.banking_system.service.AccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountService accountService;
    private final TransactionRepository transactionRepository;

    public AccountController(AccountService accountService, TransactionRepository transactionRepository) {
        this.accountService = accountService;
        this.transactionRepository = transactionRepository;
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transferMoney(@RequestBody TransferRequest transferRequest) {
        try{
            accountService.transfer(transferRequest.getFromAccountId(),
            transferRequest.getToAccountId(),
            transferRequest.getAmount());
            return ResponseEntity.ok("轉帳成功");
        }
        catch (RuntimeException e){
            return ResponseEntity.badRequest().body("轉帳失敗: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public AccountResponse getAccount(@PathVariable Long id){
        Account account = accountService.getAccountById(id);
        AccountResponse accountResponse = new AccountResponse(account.getId(), account.getAccountNumber(), account.getBalance());
        return accountResponse;
    }

    @GetMapping("/{accountId}/transactions")
    public List<Transaction> getTransactions(@PathVariable Long accountId) {
        return transactionRepository.findByFromAccountIdOrToAccountIdOrderByTimestampDesc(accountId, accountId);
    }
}
