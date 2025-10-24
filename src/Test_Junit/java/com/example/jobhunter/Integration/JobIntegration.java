package com.example.jobhunter.Integration;

import com.example.jobhunter.domain.Job;
import com.example.jobhunter.domain.Skills;
import com.example.jobhunter.domain.SkillRepository;
import com.example.jobhunter.dto.request.ReqCreateJobDTO;
import com.example.jobhunter.dto.response.job.ResCreateJobDTO;
import com.example.jobhunter.repository.JobRepository;
import com.example.jobhunter.service.JobService;
import com.example.jobhunter.util.constant.LevelEnum;
import com.example.jobhunter.util.constant.StatusEnum;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("Job Integration Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JobIntegrationTest {

    @Autowired
    private JobService jobService;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private SkillRepository skillRepository;

    private Skills skill1;
    private Skills skill2;

    @BeforeEach
    void setUp() {
        // Clear database
        jobRepository.deleteAll();
        skillRepository.deleteAll();

        // Create skills
        skill1 = new Skills();
        skill1.setName("Java");
        skill1 = skillRepository.save(skill1);

        skill2 = new Skills();
        skill2.setName("Spring Boot");
        skill2 = skillRepository.save(skill2);
    }

    @Test
    @Order(1)
    @DisplayName("Integration Test 1: Tạo job và lưu vào database")
    void testCreateJob_SaveToDatabase() {
        // Given
        ReqCreateJobDTO request = new ReqCreateJobDTO();
        request.setName("Full Stack Developer");
        request.setSalary("2000.0");
        request.setLocation("Hồ Chí Minh");
        request.setJobType("Full-time");
        request.setEducationLevel(LevelEnum.BACHELOR);
        request.setDescription("Develop full stack applications");
        request.setRequirements("3+ years experience");
        request.setBenefits(Arrays.asList("Bonus", "Free lunch"));
        request.setWorkAddress("Quận 1, TP.HCM");
        request.setStartDate(new Date());
        request.setEndDate(new Date(System.currentTimeMillis() + 86400000L * 60));
        request.setJobStatus("OPEN");
        request.setSkillIds(Arrays.asList(skill1.getId(), skill2.getId()));

        // When
        ResCreateJobDTO result = jobService.registerNewJob(request);

        // Then
        assertNotNull(result);
        assertNotNull(result.getId());
        
        // Verify in database
        Job savedJob = jobRepository.findById(result.getId()).orElse(null);
        assertNotNull(savedJob);
        assertEquals("Full Stack Developer", savedJob.getName());
        assertEquals(StatusEnum.ACTIVE, savedJob.getStatus());
        assertEquals(2, savedJob.getSkills().size());
    }

    @Test
    @Order(2)
    @DisplayName("Integration Test 2: Update job trong database")
    void testUpdateJob_InDatabase() {
        // Given - Create a job first
        Job job = new Job();
        job.setName("Frontend Developer");
        job.setSalary("1200.0");
        job.setLocation("Đà Nẵng");
        job.setActive(true);
        job.setStatus(StatusEnum.ACTIVE);
        job.setSkills(Arrays.asList(skill1));
        job = jobRepository.save(job);

        // When - Update the job
        job.setName("Senior Frontend Developer");
        job.setSalary("1800.0");
        jobService.handleSaveJob(job);

        // Then
        Job updatedJob = jobRepository.findById(job.getId()).orElse(null);
        assertNotNull(updatedJob);
        assertEquals("Senior Frontend Developer", updatedJob.getName());
        assertEquals(1800.0, updatedJob.getSalary());
    }

    @Test
    @Order(3)
    @DisplayName("Integration Test 3: Delete job từ database")
    void testDeleteJob_FromDatabase() {
        // Given
        Job job = new Job();
        job.setName("Temporary Job");
        job.setSalary("1000.0");
        job.setActive(true);
        job.setStatus(StatusEnum.ACTIVE);
        job = jobRepository.save(job);
        Long jobId = job.getId();

        // When
        jobService.handleDeleteJob(jobId);

        // Then
        assertFalse(jobRepository.findById(jobId).isPresent());
    }

    @Test
    @Order(4)
    @DisplayName("Integration Test 4: Fetch all jobs với pagination")
    void testFetchAllJobs_WithPagination() {
        // Given - Create multiple jobs
        for (int i = 1; i <= 5; i++) {
            Job job = new Job();
            job.setName("Job " + i);
            job.setSalary(String.valueOf(1000.0 + i * 100));
            job.setActive(true);
            job.setStatus(StatusEnum.ACTIVE);
            jobRepository.save(job);
        }

        // When
        List<Job> jobs = jobRepository.findAll();

        // Then
        assertEquals(5, jobs.size());
    }
}