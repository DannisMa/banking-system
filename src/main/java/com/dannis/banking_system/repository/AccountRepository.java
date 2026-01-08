package com.dannis.banking_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dannis.banking_system.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByUserId(Long userId);//查某人所有帳戶
    Account findByAccountNumber(String accountNumber);//查詢特定帳號
}
