package ru.menshikova.webgateway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.menshikova.webgateway.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}