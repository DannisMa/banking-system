package com.dannis.banking_system.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dannis.banking_system.dto.TransferRequest;
import com.dannis.banking_system.model.Account;
import com.dannis.banking_system.repository.AccountRepository;
import com.dannis.banking_system.service.AccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;



@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountService accountService;
    private final AccountRepository accountRepository;

    public AccountController(AccountService accountService, AccountRepository accountRepository) {
        this.accountService = accountService;
        this.accountRepository = accountRepository;
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
    public Account getAccount(@PathVariable Long id){
        return accountRepository.findById(id).orElseThrow(() -> new RuntimeException("找不到帳戶 ID: " + id));
    }
}
