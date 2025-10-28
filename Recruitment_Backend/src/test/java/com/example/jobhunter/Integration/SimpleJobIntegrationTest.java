package com.example.jobhunter.Integration;

import com.example.jobhunter.domain.Job;
import com.example.jobhunter.domain.Company;
import com.example.jobhunter.repository.JobRepository;
import com.example.jobhunter.repository.CompanyRepository;
import com.example.jobhunter.util.constant.StatusEnum;
import com.example.jobhunter.util.constant.LevelEnum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Simple Job Integration Tests")
class SimpleJobIntegrationTest {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private CompanyRepository companyRepository;

    private Company testCompany;

    @BeforeEach
    void setUp() {
        jobRepository.deleteAll();
        companyRepository.deleteAll();
        
        // Tạo company test
        testCompany = new Company();
        testCompany.setName("Test Company");
        testCompany.setDescription("Test Company Description");
        testCompany = companyRepository.save(testCompany);
    }

    @Test
    @DisplayName("Test 1: Tạo và lưu job vào database")
    void testCreateJob() {
        // Given
        Job job = new Job();
        job.setName("Java Developer");
        job.setDescription("Java Developer Position");
        job.setLocation("Hà Nội");
        job.setSalary("1500.0");
        job.setStatus(StatusEnum.ACTIVE);

        // When
        Job savedJob = jobRepository.save(job);

        // Then
        assertNotNull(savedJob.getId());
        assertEquals("Java Developer", savedJob.getName());
        assertEquals("Hà Nội", savedJob.getLocation());
        assertEquals(StatusEnum.ACTIVE, savedJob.getStatus());
    }

    @Test
    @DisplayName("Test 2: Tìm job theo tên")
    void testFindJobsByName() {
        // Given
        Job job = new Job();
        job.setName("Python Developer");
        job.setDescription("Python Position");
        job.setLocation("TP.HCM");
        job.setSalary("1200.0");
       
       
        job.setStatus(StatusEnum.ACTIVE);
       
        jobRepository.save(job);

        // When
        var jobs = jobRepository.findByName("Python Developer");

        // Then
        assertFalse(jobs.isEmpty());
        assertEquals("Python Developer", jobs.get(0).getName());
    }


    @Test
    @DisplayName("Test 5: Delete job")
    void testDeleteJob() {
        // Given
        Job job = new Job();
        job.setName("Delete Job");
        job.setDescription("To be deleted");
        job.setLocation("Hà Nội");
        job.setSalary("1000.0");
        
        job.setStatus(StatusEnum.ACTIVE);
        
        Job savedJob = jobRepository.save(job);
        Long jobId = savedJob.getId();

        // When
        jobRepository.deleteById(jobId);

        // Then
        assertFalse(jobRepository.findById(jobId).isPresent());
    }
}