package com.example.jobhunter.repository;

import com.example.jobhunter.domain.UserPortfolio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPortfolioRepository extends JpaRepository<UserPortfolio, Long> {
    Optional<UserPortfolio> findByUserId(Long userId);
}