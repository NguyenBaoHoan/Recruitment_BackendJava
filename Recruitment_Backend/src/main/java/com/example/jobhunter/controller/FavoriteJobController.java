package com.example.jobhunter.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.jobhunter.dto.response.ResFavoriteJobDTO;
import com.example.jobhunter.service.FavoriteJobService;
import com.example.jobhunter.util.FormatRestResponse;

@RestController
@RequestMapping("/api/favorite-jobs")
public class FavoriteJobController {

    @Autowired
    private FavoriteJobService favoriteJobService;

    /**
     * Lấy danh sách job yêu thích của user
     * GET /api/favorite-jobs?userId=1
     */
    @GetMapping
    public ResponseEntity<?> getFavoriteJobs(@RequestParam("userId") long userId) {
        try {
            List<ResFavoriteJobDTO> favoriteJobs = favoriteJobService.getFavoriteJobs(userId);
            return ResponseEntity.ok(favoriteJobs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Thêm job vào danh sách yêu thích
     * POST /api/favorite-jobs/add?userId=1&jobId=1
     */
    @PostMapping("/add")
    public ResponseEntity<?> addToFavorites(@RequestParam("userId") long userId, @RequestParam("jobId") long jobId) {
        try {
            favoriteJobService.addToFavorites(userId, jobId);
            return ResponseEntity.ok("Thêm job vào danh sách yêu thích thành công");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Xóa job khỏi danh sách yêu thích
     * DELETE /api/favorite-jobs/remove?userId=1&jobId=1
     */
    @DeleteMapping("/remove")
    public ResponseEntity<?> removeFromFavorites(@RequestParam("userId") long userId, @RequestParam("jobId") long jobId) {
        try {
            favoriteJobService.removeFromFavorites(userId, jobId);
            return ResponseEntity.ok("Xóa job khỏi danh sách yêu thích thành công");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Kiểm tra job có được yêu thích không
     * GET /api/favorite-jobs/check?userId=1&jobId=1
     */
    @GetMapping("/check")
    public ResponseEntity<?> checkIfFavorited(@RequestParam("userId") long userId, @RequestParam("jobId") long jobId) {
        try {
            boolean isFavorited = favoriteJobService.isJobFavorited(userId, jobId);
            return ResponseEntity.ok(isFavorited);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Đếm số job yêu thích của user
     * GET /api/favorite-jobs/count?userId=1
     */
    @GetMapping("/count")
    public ResponseEntity<?> countFavoriteJobs(@RequestParam("userId") long userId) {
        try {
            long count = favoriteJobService.countFavoriteJobs(userId);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}