package com.dannis.banking_system.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dannis.banking_system.model.User;

public interface UserRepository extends JpaRepository<User, Long>  {
    Optional<User> findByUsername(String username);//查使用者名稱
    boolean existsByEmail(String email);//檢查email是否存在
}
