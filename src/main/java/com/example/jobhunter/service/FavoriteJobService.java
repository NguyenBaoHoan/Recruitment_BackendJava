package com.example.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.jobhunter.domain.FavoriteJob;
import com.example.jobhunter.domain.Job;
import com.example.jobhunter.domain.User;
import com.example.jobhunter.dto.response.ResFavoriteJobDTO;
import com.example.jobhunter.repository.FavoriteJobRepository;
import com.example.jobhunter.repository.JobRepository;
import com.example.jobhunter.repository.UserRepository;
import com.example.jobhunter.util.error.IdInvalidException;

@Service
public class FavoriteJobService {

    @Autowired
    private FavoriteJobRepository favoriteJobRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Lấy danh sách job yêu thích của user
     */
    public List<ResFavoriteJobDTO> getFavoriteJobs(long userId) {
        // Kiểm tra user có tồn tại không
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IdInvalidException("User khaông tồn tại"));

        List<FavoriteJob> favoriteJobs = favoriteJobRepository.findByUserId(userId);

        return favoriteJobs.stream()
                .map(favoriteJob -> ResFavoriteJobDTO.fromJob(
                        favoriteJob.getJob(),
                        favoriteJob.getCreatedAt()))
                .collect(Collectors.toList());
    }

    /**
     * Thêm job vào danh sách yêu thích
     */
    public void addToFavorites(long userId, long jobId) {
        // Kiểm tra user có tồn tại không
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IdInvalidException("User không tồn tại"));

        // Kiểm tra job có tồn tại không
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IdInvalidException("Job không tồn tại"));

        // Kiểm tra job đã được yêu thích chưa
        List<FavoriteJob> existingFavorites = favoriteJobRepository.findByUserIdAndJobId(userId, jobId);
        if (!existingFavorites.isEmpty()) {
            throw new IdInvalidException("Job đã được yêu thích trước đó");
        }

        // Tạo favorite job mới
        FavoriteJob favoriteJob = new FavoriteJob();
        favoriteJob.setUser(user);
        favoriteJob.setJob(job);

        favoriteJobRepository.save(favoriteJob);
    }

    /**
     * Xóa job khỏi danh sách yêu thích
     */
    @Transactional
    public void removeFromFavorites(long userId, long jobId) {
        // Kiểm tra user có tồn tại không
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IdInvalidException("User không tồn tại"));

        // Kiểm tra job có tồn tại không
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IdInvalidException("Job không tồn tại"));

        // Kiểm tra job đã được yêu thích chưa
        List<FavoriteJob> existingFavorites = favoriteJobRepository.findByUserIdAndJobId(userId, jobId);
        if (existingFavorites.isEmpty()) {
            throw new IdInvalidException("Job chưa được yêu thích");
        }

        // Xóa tất cả favorite job records (nếu có duplicate)
        favoriteJobRepository.deleteByUserIdAndJobId(userId, jobId);
    }

    /**
     * Kiểm tra job có được yêu thích không
     */
    public boolean isJobFavorited(long userId, long jobId) {
        List<FavoriteJob> favoriteJobs = favoriteJobRepository.findByUserIdAndJobId(userId, jobId);
        return !favoriteJobs.isEmpty();
    }

    /**
     * Đếm số job yêu thích của user
     */
    public long countFavoriteJobs(long userId) {
        return favoriteJobRepository.countByUserId(userId);
    }
}