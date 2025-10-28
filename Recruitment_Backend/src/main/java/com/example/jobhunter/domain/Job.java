package com.example.jobhunter.domain;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import com.example.jobhunter.util.constant.LevelEnum;
import com.example.jobhunter.util.constant.StatusEnum;
import com.example.jobhunter.util.error.SecurityUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

@Table(name = "jobs")
@Entity
@Data
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String location;
    private String salary;
    @Enumerated(EnumType.STRING)
    private LevelEnum educationLevel;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String jobType;
    // Trong entity Job, thêm annotation cho các field List
    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String requirements;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String benefits;
    private String workAddress;
    private Date startDate;
    private Date endDate;
    private boolean isActive;
    @Enumerated(EnumType.STRING)
    private StatusEnum status; // ACTIVE, INACTIVE, PENDING, etc.
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
    private String jobStatus;
    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "jobs", "hibernateLazyInitializer", "handler" })
    @JoinTable(name = "job_skills", joinColumns = @JoinColumn(name = "job_id"), inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private List<Skills> skills;

    @PrePersist
    public void handleCreateAt() {
        this.createdBy = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        this.createdAt = Instant.now();

    }

    @PreUpdate
    public void handleUpdateAt() {
        this.updatedBy = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        this.updatedAt = Instant.now();
    }
}
