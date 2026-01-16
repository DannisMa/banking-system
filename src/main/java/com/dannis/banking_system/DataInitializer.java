//Dummy Data for testing purpose
package com.dannis.banking_system;

import java.math.BigDecimal;
import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.dannis.banking_system.model.Account;
import com.dannis.banking_system.model.User;
import com.dannis.banking_system.repository.AccountRepository;
import com.dannis.banking_system.repository.UserRepository;
import com.dannis.banking_system.service.AccountService;

@Component
public class DataInitializer implements CommandLineRunner{
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    private final AccountService accountService;

    public DataInitializer(UserRepository userRepository, AccountRepository accountRepository, AccountService accountService) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.accountService = accountService;
    }

    @Override
    public void run(String... args) throws Exception {
        if(userRepository.count() > 0){
            System.out.println("資料庫已初始化，跳過資料建立。");
            return;
        }
        User user1 = new User(null, "dannis", "123456", "dannis@example.com");
        User user2 = new User(null,"alice" , "password", "alice@example.com");
        userRepository.saveAll(Arrays.asList(user1, user2));

        Account account1 = new Account(null, "888-111-222", new BigDecimal("100000.00"), user1);
        Account account2 = new Account(null, "999-333-444", new BigDecimal("50000.00"), user2);
        accountRepository.saveAll(Arrays.asList(account1, account2));

        System.out.println("資料庫初始化完成！已建立 2 個使用者與 2 個帳戶。");

        System.out.println("進行測試轉帳：從 dannis 轉帳 200.00 到 alice");
        try{
            accountService.transfer(account1.getId(), account2.getId(), new BigDecimal("200.00"));
        }catch(Exception e){
            System.out.println("轉帳失敗: " + e.getMessage());
        }
    }
    
}
