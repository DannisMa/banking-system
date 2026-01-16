package com.dannis.banking_system.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dannis.banking_system.dto.AccountResponse;
import com.dannis.banking_system.dto.TranactionsRespone;
import com.dannis.banking_system.dto.TransferRequest;
import com.dannis.banking_system.model.Account;
import com.dannis.banking_system.model.Transaction;
import com.dannis.banking_system.service.AccountService;
import com.dannis.banking_system.service.TransactionService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountService accountService;
    private final TransactionService transactionService;

    public AccountController(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
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
    public List<TranactionsRespone> getTransactions(@PathVariable Long accountId) {
        List<Transaction> transactions = transactionService.getTransactionsForAccount(accountId);
        return transactions.stream().map(transaction -> new TranactionsRespone(
                transaction.getId(),
                transaction.getFromAccountId(),
                transaction.getToAccountId(),
                transaction.getAmount(),
                transaction.getTimestamp()
        )).toList();
    }
}
