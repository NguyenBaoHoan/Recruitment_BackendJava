package com.example.jobhunter.service;

import com.example.jobhunter.domain.CareerExpectation;
import com.example.jobhunter.domain.User;
import com.example.jobhunter.dto.request.ReqCareerExpectationDTO;
import com.example.jobhunter.dto.response.ResCareerExpectationDTO;
import com.example.jobhunter.repository.CareerExpectationRepository;
import com.example.jobhunter.util.error.IdInvalidException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CareerExpectationService {
    
    private final CareerExpectationRepository careerExpectationRepository;
    private final UserService userService;
    private static final int MAX_EXPECTATIONS = 3;

    public CareerExpectationService(
            CareerExpectationRepository careerExpectationRepository,
            UserService userService) {
        this.careerExpectationRepository = careerExpectationRepository;
        this.userService = userService;
    }

    public ResCareerExpectationDTO createCareerExpectation(Long userId, ReqCareerExpectationDTO request) 
            throws IdInvalidException {
        
        // Kiểm tra user tồn tại
        User user = userService.fetchOneUser(userId);
        if (user == null) {
            throw new IdInvalidException("Không tìm thấy người dùng");
        }

        // Kiểm tra giới hạn 3 kỳ vọng
        long currentCount = careerExpectationRepository.countByUser(user);
        if (currentCount >= MAX_EXPECTATIONS) {
            throw new IdInvalidException("Bạn chỉ có thể tạo tối đa " + MAX_EXPECTATIONS + " kỳ vọng nghề nghiệp");
        }

        // Tạo mới
        CareerExpectation expectation = new CareerExpectation();
        expectation.setUser(user);
        expectation.setJobType(request.getJobType());
        expectation.setDesiredPosition(request.getDesiredPosition());
        expectation.setDesiredIndustry(request.getDesiredIndustry());
        expectation.setDesiredCity(request.getDesiredCity());
        expectation.setMinSalary(request.getMinSalary());
        expectation.setMaxSalary(request.getMaxSalary());

        CareerExpectation saved = careerExpectationRepository.save(expectation);
        return new ResCareerExpectationDTO(saved);
    }

    public List<ResCareerExpectationDTO> getCareerExpectationsByUserId(Long userId) {
        return careerExpectationRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(ResCareerExpectationDTO::new)
                .collect(Collectors.toList());
    }

    public ResCareerExpectationDTO updateCareerExpectation(
            Long userId, Long expectationId, ReqCareerExpectationDTO request) throws IdInvalidException {
        
        // Kiểm tra quyền sở hữu
        if (!careerExpectationRepository.existsByIdAndUserId(expectationId, userId)) {
            throw new IdInvalidException("Không tìm thấy kỳ vọng nghề nghiệp hoặc bạn không có quyền chỉnh sửa");
        }

        CareerExpectation expectation = careerExpectationRepository.findById(expectationId)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy kỳ vọng nghề nghiệp"));

        // Cập nhật thông tin
        expectation.setJobType(request.getJobType());
        expectation.setDesiredPosition(request.getDesiredPosition());
        expectation.setDesiredIndustry(request.getDesiredIndustry());
        expectation.setDesiredCity(request.getDesiredCity());
        expectation.setMinSalary(request.getMinSalary());
        expectation.setMaxSalary(request.getMaxSalary());

        CareerExpectation updated = careerExpectationRepository.save(expectation);
        return new ResCareerExpectationDTO(updated);
    }

    public void deleteCareerExpectation(Long userId, Long expectationId) throws IdInvalidException {
        // Kiểm tra quyền sở hữu
        if (!careerExpectationRepository.existsByIdAndUserId(expectationId, userId)) {
            throw new IdInvalidException("Không tìm thấy kỳ vọng nghề nghiệp hoặc bạn không có quyền xóa");
        }

        careerExpectationRepository.deleteById(expectationId);
    }

    public long getCareerExpectationCount(Long userId) {
        return careerExpectationRepository.countByUserId(userId);
    }

    // Cập nhật trạng thái tìm việc
    public void updateJobSeekingStatus(Long userId, User.JobSeekingStatus status) throws IdInvalidException {
        User user = userService.fetchOneUser(userId);
        if (user == null) {
            throw new IdInvalidException("Không tìm thấy người dùng");
        }

        user.setJobSeekingStatus(status);
        userService.handleSaveUser(user);
    }

    public User.JobSeekingStatus getJobSeekingStatus(Long userId) throws IdInvalidException {
        User user = userService.fetchOneUser(userId);
        if (user == null) {
            throw new IdInvalidException("Không tìm thấy người dùng");
        }

        return user.getJobSeekingStatus() != null ? user.getJobSeekingStatus() : User.JobSeekingStatus.READY_NOW;
    }
}