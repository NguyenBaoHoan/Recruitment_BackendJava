package com.example.jobhunter.controller;

import com.example.jobhunter.domain.User;
import com.example.jobhunter.dto.request.ReqCareerExpectationDTO;
import com.example.jobhunter.dto.request.ReqJobSeekingStatusDTO;
import com.example.jobhunter.dto.response.ResCareerExpectationDTO;
import com.example.jobhunter.service.CareerExpectationService;
import com.example.jobhunter.util.anotation.ApiMessage;
import com.example.jobhunter.util.error.IdInvalidException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/career-expectations")
public class CareerExpectationController {

    private final CareerExpectationService careerExpectationService;

    public CareerExpectationController(CareerExpectationService careerExpectationService) {
        this.careerExpectationService = careerExpectationService;
    }

    @PostMapping("/user/{userId}")
    @ApiMessage("Tạo kỳ vọng nghề nghiệp mới")
    public ResponseEntity<ResCareerExpectationDTO> createCareerExpectation(
            @PathVariable Long userId,
            @Valid @RequestBody ReqCareerExpectationDTO request) throws IdInvalidException {
        
        ResCareerExpectationDTO result = careerExpectationService.createCareerExpectation(userId, request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/user/{userId}")
    @ApiMessage("Lấy danh sách kỳ vọng nghề nghiệp của người dùng")
    public ResponseEntity<Map<String, Object>> getCareerExpectations(@PathVariable Long userId) throws IdInvalidException {
        
        List<ResCareerExpectationDTO> expectations = careerExpectationService.getCareerExpectationsByUserId(userId);
        long count = careerExpectationService.getCareerExpectationCount(userId);
        User.JobSeekingStatus status = careerExpectationService.getJobSeekingStatus(userId);

        Map<String, Object> response = new HashMap<>();
        response.put("expectations", expectations);
        response.put("count", count);
        response.put("maxCount", 3);
        response.put("jobSeekingStatus", status);
        response.put("jobSeekingStatusDisplay", status.getDisplayName());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{expectationId}/user/{userId}")
    @ApiMessage("Cập nhật kỳ vọng nghề nghiệp")
    public ResponseEntity<ResCareerExpectationDTO> updateCareerExpectation(
            @PathVariable Long userId,
            @PathVariable Long expectationId,
            @Valid @RequestBody ReqCareerExpectationDTO request) throws IdInvalidException {
        
        ResCareerExpectationDTO result = careerExpectationService.updateCareerExpectation(userId, expectationId, request);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{expectationId}/user/{userId}")
    @ApiMessage("Xóa kỳ vọng nghề nghiệp")
    public ResponseEntity<String> deleteCareerExpectation(
            @PathVariable Long userId,
            @PathVariable Long expectationId) throws IdInvalidException {
        
        careerExpectationService.deleteCareerExpectation(userId, expectationId);
        return ResponseEntity.ok("Đã xóa kỳ vọng nghề nghiệp thành công");
    }

    @PutMapping("/job-seeking-status/user/{userId}")
    @ApiMessage("Cập nhật trạng thái tìm việc")
    public ResponseEntity<Map<String, Object>> updateJobSeekingStatus(
            @PathVariable Long userId,
            @Valid @RequestBody ReqJobSeekingStatusDTO request) throws IdInvalidException {
        
        careerExpectationService.updateJobSeekingStatus(userId, request.getJobSeekingStatus());
        
        Map<String, Object> response = new HashMap<>();
        response.put("jobSeekingStatus", request.getJobSeekingStatus());
        response.put("jobSeekingStatusDisplay", request.getJobSeekingStatus().getDisplayName());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/options")
    @ApiMessage("Lấy các tùy chọn cho form")
    public ResponseEntity<Map<String, Object>> getFormOptions() {
        Map<String, Object> options = new HashMap<>();
        
        // Job types
        Map<String, String> jobTypes = new HashMap<>();
        for (com.example.jobhunter.domain.CareerExpectation.JobType type : com.example.jobhunter.domain.CareerExpectation.JobType.values()) {
            jobTypes.put(type.name(), type.getDisplayName());
        }
        
        // Job seeking statuses
        Map<String, String> jobSeekingStatuses = new HashMap<>();
        for (User.JobSeekingStatus status : User.JobSeekingStatus.values()) {
            jobSeekingStatuses.put(status.name(), status.getDisplayName());
        }

        options.put("jobTypes", jobTypes);
        options.put("jobSeekingStatuses", jobSeekingStatuses);

        return ResponseEntity.ok(options);
    }
}