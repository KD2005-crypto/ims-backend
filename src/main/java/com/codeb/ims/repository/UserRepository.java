package com.codeb.ims.repository;

import com.codeb.ims.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // This is the UPGRADE.
    // We need this to find users by email when they try to login.
    Optional<User> findByEmail(String email);
}