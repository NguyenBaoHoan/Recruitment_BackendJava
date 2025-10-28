package com.example.jobhunter.repository;

import com.example.jobhunter.domain.UserPortfolioImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserPortfolioImageRepository extends JpaRepository<UserPortfolioImage, Long> {
    List<UserPortfolioImage> findByUserIdOrderByOrderIndexAscCreatedAtAsc(Long userId);
    int countByUserId(Long userId);
}