package com.example.jobhunter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.jobhunter.domain.Skills;

@Repository
public interface SkillRepository extends JpaRepository<Skills, Long>, JpaSpecificationExecutor<Skills>{
    
}
