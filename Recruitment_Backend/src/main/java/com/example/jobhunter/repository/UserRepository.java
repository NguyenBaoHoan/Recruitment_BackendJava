package com.example.jobhunter.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.jobhunter.domain.Company;
import com.example.jobhunter.domain.User;
import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User>{
    User findByEmail(String email);
    Boolean existsByEmail(String email);
    User findByRefreshTokenAndEmail(String token, String email);
    List<User> findByCompany(Company company);
    
}
