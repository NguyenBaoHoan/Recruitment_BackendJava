package com.example.jobhunter.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.jobhunter.domain.Job;
import com.example.jobhunter.repository.JobRepository;
import com.example.jobhunter.util.constant.StatusEnum;

/**
 * Service để xử lý các scheduled tasks liên quan đến Job
 * Chịu trách nhiệm tự động deactivate các job hết hạn
 */
@Service
@Transactional
public class JobSchedulerService {

    private static final Logger logger = LoggerFactory.getLogger(JobSchedulerService.class);

    @Autowired
    private JobRepository jobRepository;

    /**
     * Scheduled task chạy mỗi ngày lúc 00:00 để kiểm tra và deactivate các job hết hạn
     * Cron expression: "0 0 0 * * ?" = giây 0, phút 0, giờ 0, mọi ngày, mọi tháng, mọi thứ
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void deactivateExpiredJobs() {
        logger.info("Starting scheduled task to deactivate expired jobs...");
        
        try {
            // Lấy ngày hiện tại
            LocalDate today = LocalDate.now();
            Date currentDate = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
            
            // Tìm tất cả jobs đang active và có endDate < ngày hiện tại
            List<Job> expiredJobs = jobRepository.findActiveJobsWithEndDateBefore(currentDate);
            
            if (expiredJobs.isEmpty()) {
                logger.info("No expired jobs found.");
                return;
            }
            
            logger.info("Found {} expired jobs to deactivate", expiredJobs.size());
            
            // Deactivate các jobs hết hạn
            int deactivatedCount = 0;
            for (Job job : expiredJobs) {
                job.setActive(false);
                job.setStatus(StatusEnum.INACTIVE);
                jobRepository.save(job);
                deactivatedCount++;
                
                logger.info("Deactivated job: ID={}, Name='{}', EndDate='{}'", 
                    job.getId(), job.getName(), job.getEndDate());
            }
            
            logger.info("Successfully deactivated {} expired jobs", deactivatedCount);
            
        } catch (Exception e) {
            logger.error("Error occurred while deactivating expired jobs: {}", e.getMessage(), e);
        }
    }

    /**
     * Method để test hoặc trigger manual việc deactivate expired jobs
     * Có thể được gọi từ controller hoặc testing
     */
    public int manualDeactivateExpiredJobs() {
        logger.info("Manual trigger to deactivate expired jobs...");
        
        LocalDate today = LocalDate.now();
        Date currentDate = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
        
        List<Job> expiredJobs = jobRepository.findActiveJobsWithEndDateBefore(currentDate);
        
        int deactivatedCount = 0;
        for (Job job : expiredJobs) {
            job.setActive(false);
            job.setStatus(StatusEnum.INACTIVE);
            jobRepository.save(job);
            deactivatedCount++;
        }
        
        logger.info("Manually deactivated {} expired jobs", deactivatedCount);
        return deactivatedCount;
    }

    /**
     * Scheduled task chạy mỗi giờ để log thống kê jobs
     * Để monitoring và debug
     */
    @Scheduled(fixedRate = 3600000) // Chạy mỗi giờ (3600000 ms)
    public void logJobStatistics() {
        try {
            long totalJobs = jobRepository.count();
            List<Job> activeJobs = jobRepository.findByIsActiveAndStatus(true, StatusEnum.ACTIVE);
            List<Job> inactiveJobs = jobRepository.findByIsActiveAndStatus(false, StatusEnum.INACTIVE);
            
            logger.info("Job Statistics - Total: {}, Active: {}, Inactive: {}", 
                totalJobs, activeJobs.size(), inactiveJobs.size());
                
        } catch (Exception e) {
            logger.error("Error logging job statistics: {}", e.getMessage());
        }
    }
}