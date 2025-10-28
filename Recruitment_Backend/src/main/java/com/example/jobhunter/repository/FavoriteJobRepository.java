package com.example.jobhunter.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.jobhunter.domain.FavoriteJob;
import com.example.jobhunter.domain.Job;
import com.example.jobhunter.domain.User;

@Repository
public interface FavoriteJobRepository extends JpaRepository<FavoriteJob, Long> {

    // Lấy danh sách job yêu thích của user
    @Query("SELECT fj FROM FavoriteJob fj WHERE fj.user.id = :userId")
    List<FavoriteJob> findByUserId(@Param("userId") long userId);

    // Kiểm tra job đã được yêu thích chưa - lấy record đầu tiên nếu có nhiều
    @Query("SELECT fj FROM FavoriteJob fj WHERE fj.user.id = :userId AND fj.job.id = :jobId")
    List<FavoriteJob> findByUserIdAndJobId(@Param("userId") long userId, @Param("jobId") long jobId);
    void deleteByUserIdAndJobId(long userId, long jobId);

    // Đếm số job yêu thích của user
    @Query("SELECT COUNT(fj) FROM FavoriteJob fj WHERE fj.user.id = :userId")
    long countByUserId(@Param("userId") long userId);
}