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

@Component
public class DataInitializer implements CommandLineRunner{
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    public DataInitializer(UserRepository userRepository, AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        User user1 = new User(null, "dannis", "123456", "dannis@example.com");
        User user2 = new User(null,"alice" , "password", "alice@example.com");
        userRepository.saveAll(Arrays.asList(user1, user2));

        Account account1 = new Account(null, "888-111-222", new BigDecimal("1000.00"), user1);
        Account account2 = new Account(null, "999-333-444", new BigDecimal("500.00"), user2);
        accountRepository.saveAll(Arrays.asList(account1, account2));

        System.out.println("資料庫初始化完成！已建立 2 個使用者與 2 個帳戶。");
    }
    
}
