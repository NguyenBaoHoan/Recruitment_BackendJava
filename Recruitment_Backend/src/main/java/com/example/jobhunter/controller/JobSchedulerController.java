package com.example.jobhunter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jobhunter.service.JobSchedulerService;
import com.example.jobhunter.util.anotation.ApiMessage;

/**
 * Controller để quản lý các scheduled tasks của Job
 * Chỉ admin mới có thể trigger manual các tasks
 */
@RestController
@RequestMapping("/api/v1/admin/job-scheduler")
public class JobSchedulerController {

    @Autowired
    private JobSchedulerService jobSchedulerService;

    /**
     * Trigger manual việc deactivate expired jobs
     * POST /api/v1/admin/job-scheduler/deactivate-expired
     */
    @PostMapping("/deactivate-expired")
    @ApiMessage("Manually deactivate expired jobs")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> manualDeactivateExpiredJobs() {
        try {
            int deactivatedCount = jobSchedulerService.manualDeactivateExpiredJobs();
            
            return ResponseEntity.ok().body(new ManualDeactivateResponse(
                "Successfully deactivated expired jobs",
                deactivatedCount,
                System.currentTimeMillis()
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error deactivating expired jobs: " + e.getMessage());
        }
    }

    /**
     * Response class cho manual deactivate
     */
    public static class ManualDeactivateResponse {
        private String message;
        private int deactivatedCount;
        private long timestamp;

        public ManualDeactivateResponse(String message, int deactivatedCount, long timestamp) {
            this.message = message;
            this.deactivatedCount = deactivatedCount;
            this.timestamp = timestamp;
        }

        // Getters and setters
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        
        public int getDeactivatedCount() { return deactivatedCount; }
        public void setDeactivatedCount(int deactivatedCount) { this.deactivatedCount = deactivatedCount; }
        
        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    }
}