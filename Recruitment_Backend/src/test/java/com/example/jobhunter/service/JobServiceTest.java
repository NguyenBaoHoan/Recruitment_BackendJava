package com.example.jobhunter.service;
import com.example.jobhunter.domain.Skills;
import com.example.jobhunter.domain.Job;
import com.example.jobhunter.domain.SkillRepository;
import com.example.jobhunter.dto.request.ReqCreateJobDTO;
import com.example.jobhunter.dto.response.job.ResCreateJobDTO;
import com.example.jobhunter.repository.JobRepository;

import com.example.jobhunter.util.constant.LevelEnum;
import com.example.jobhunter.util.constant.StatusEnum;
import com.example.jobhunter.util.error.IdInvalidException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("JobService Unit Tests")
class JobServiceTest {

    @Mock
    private JobRepository jobRepository;

    @Mock
    private SkillRepository skillRepository;

    @InjectMocks
    private JobService jobService;

    private ReqCreateJobDTO validJobRequest;
    private Skills skill1;
    private Skills skill2;
    private Job savedJob;

    @BeforeEach
    void setUp() {
        // Prepare test data
        skill1 = new Skills();
        skill1.setId(1L);
        skill1.setName("Java");

        skill2 = new Skills();
        skill2.setId(2L);
        skill2.setName("Spring Boot");

        validJobRequest = new ReqCreateJobDTO();
        validJobRequest.setName("Backend Developer");
        validJobRequest.setSalary("1500.0");
        validJobRequest.setLocation("Hà Nội");
        validJobRequest.setJobType("Full-time");
        validJobRequest.setEducationLevel(LevelEnum.BACHELOR);
        validJobRequest.setDescription("Develop backend systems");
        validJobRequest.setRequirements("2+ years experience");
        validJobRequest.setBenefits(Arrays.asList("Health Insurance", "13th month salary"));
        validJobRequest.setWorkAddress("Cầu Giấy, Hà Nội");
        validJobRequest.setStartDate(new Date());
        validJobRequest.setEndDate(new Date(System.currentTimeMillis() + 86400000L * 30));
        validJobRequest.setJobStatus("OPEN");
        validJobRequest.setSkillIds(Arrays.asList(1L, 2L));

        savedJob = new Job();
        savedJob.setId(1L);
        savedJob.setName("Backend Developer");
        savedJob.setSalary("1500.0");
        savedJob.setStatus(StatusEnum.ACTIVE);
        savedJob.setSkills(Arrays.asList(skill1, skill2));
    }

    @Test
    @DisplayName("Test 1: Tạo job thành công với đầy đủ thông tin")
    void testRegisterNewJob_Success() {
        // Given
        when(skillRepository.findById(1L)).thenReturn(Optional.of(skill1));
        when(skillRepository.findById(2L)).thenReturn(Optional.of(skill2));
        when(jobRepository.save(any(Job.class))).thenReturn(savedJob);

        // When
        ResCreateJobDTO result = jobService.registerNewJob(validJobRequest);

        // Then
        assertNotNull(result);
        assertEquals("Backend Developer", result.getName());
        assertEquals("1500.0", result.getSalary());
        assertEquals(2, result.getSkills().size());
        assertTrue(result.getSkills().contains("Java"));
        assertTrue(result.getSkills().contains("Spring Boot"));

        verify(skillRepository, times(2)).findById(anyLong());
        verify(jobRepository, times(1)).save(any(Job.class));
    }

    @Test
    @DisplayName("Test 2: Tạo job thất bại khi tên rỗng")
    void testRegisterNewJob_EmptyName_ThrowsException() {
        // Given
        validJobRequest.setName("");

        // When & Then
        IdInvalidException exception = assertThrows(
            IdInvalidException.class,
            () -> jobService.registerNewJob(validJobRequest)
        );

        assertEquals("Tên công việc không được để trống", exception.getMessage());
        verify(jobRepository, never()).save(any(Job.class));
    }

    @Test
    @DisplayName("Test 3: Tạo job thất bại khi skill không tồn tại")
    void testRegisterNewJob_SkillNotFound_ThrowsException() {
        // Given
        when(skillRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        IdInvalidException exception = assertThrows(
            IdInvalidException.class,
            () -> jobService.registerNewJob(validJobRequest)
        );

        assertTrue(exception.getMessage().contains("Skill không tồn tại"));
        verify(jobRepository, never()).save(any(Job.class));
    }

    @Test
    @DisplayName("Test 4: Fetch job theo ID thành công")
    void testFetchOneJob_Success() {
        // Given
        when(jobRepository.findById(1L)).thenReturn(Optional.of(savedJob));

        // When
        Optional<Job> result = jobService.fetchOneJob(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals("Backend Developer", result.get().getName());
        verify(jobRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Test 5: Delete job thành công")
    void testDeleteJob_Success() {
        // Given
        Long jobId = 1L;
        doNothing().when(jobRepository).deleteById(jobId);

        // When
        jobService.handleDeleteJob(jobId);

        // Then
        verify(jobRepository, times(1)).deleteById(jobId);
    }
}