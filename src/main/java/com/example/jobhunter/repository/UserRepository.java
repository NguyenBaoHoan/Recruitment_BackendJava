package com.example.jobhunter.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.jobhunter.domain.User;
import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    User findByEmail(String email);
    
}
