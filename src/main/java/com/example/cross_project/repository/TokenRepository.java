package com.example.cross_project.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.cross_project.model.Token;
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByValue(String value);
}
