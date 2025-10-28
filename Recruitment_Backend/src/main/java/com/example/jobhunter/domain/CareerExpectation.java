package com.example.jobhunter.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.Instant;

@Entity
@Table(name = "career_expectations")
@Getter
@Setter
public class CareerExpectation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties(value = {"careerExpectations"})
    private User user;

    // Loại hình công việc
    @Enumerated(EnumType.STRING)
    @Column(name = "job_type")
    private JobType jobType;

    // Vị trí mong muốn
    @Column(name = "desired_position")
    private String desiredPosition;

    // Ngành nghề mong muốn
    @Column(name = "desired_industry")
    private String desiredIndustry;

    // Thành phố mong muốn
    @Column(name = "desired_city")
    private String desiredCity;

    // Mức lương tối thiểu (triệu VND)
    @Column(name = "min_salary")
    private Double minSalary;

    // Mức lương tối đa (triệu VND)
    @Column(name = "max_salary")
    private Double maxSalary;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @PrePersist
    public void handleBeforeCreate() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @PreUpdate
    public void handleBeforeUpdate() {
        this.updatedAt = Instant.now();
    }

    // Enum cho loại hình công việc
    public enum JobType {
        FULL_TIME("Toàn thời gian"),
        PART_TIME("Bán thời gian"),
        INTERNSHIP("Thực tập");

        private final String displayName;

        JobType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}