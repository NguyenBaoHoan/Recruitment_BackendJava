package com.example.jobhunter.dto.response;

import java.time.Instant;
import java.util.List;

import com.example.jobhunter.domain.Job;
import com.example.jobhunter.domain.Skills;
import com.example.jobhunter.util.constant.LevelEnum;

import lombok.Data;

@Data
public class ResFavoriteJobDTO {
    private long id;
    private String name;
    private String location;
    private String salary;
    private LevelEnum educationLevel;
    private String jobType;
    private String description;
    private String requirements;
    private String benefits;
    private String workAddress;
    private boolean isActive;
    private Instant createdAt;
    private List<Skills> skills;

    // Thông tin về thời gian yêu thích
    private Instant favoritedAt;

    public static ResFavoriteJobDTO fromJob(Job job, Instant favoritedAt) {
        ResFavoriteJobDTO dto = new ResFavoriteJobDTO();
        dto.setId(job.getId());
        dto.setName(job.getName());
        dto.setLocation(job.getLocation());
        dto.setSalary(job.getSalary());
        dto.setEducationLevel(job.getEducationLevel());
        dto.setJobType(job.getJobType());
        dto.setDescription(job.getDescription());
        dto.setRequirements(job.getRequirements());
        dto.setBenefits(job.getBenefits());
        dto.setWorkAddress(job.getWorkAddress());
        dto.setActive(job.isActive());
        dto.setCreatedAt(job.getCreatedAt());
        dto.setSkills(job.getSkills());
        dto.setFavoritedAt(favoritedAt);
        return dto;
    }
}