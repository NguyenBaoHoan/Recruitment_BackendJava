package com.example.jobhunter.repository;

import com.example.jobhunter.domain.CareerExpectation;
import com.example.jobhunter.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CareerExpectationRepository extends JpaRepository<CareerExpectation, Long> {
    
    List<CareerExpectation> findByUserOrderByCreatedAtDesc(User user);
    
    List<CareerExpectation> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    long countByUser(User user);
    
    long countByUserId(Long userId);
    
    boolean existsByIdAndUserId(Long id, Long userId);
}