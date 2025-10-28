package com.example.jobhunter.dto.response;

import com.example.jobhunter.domain.CareerExpectation;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ResCareerExpectationDTO {
    private Long id;
    private CareerExpectation.JobType jobType;
    private String jobTypeDisplay;
    private String desiredPosition;
    private String desiredIndustry;
    private String desiredCity;
    private Double minSalary;
    private Double maxSalary;
    private String salaryRange;
    private Instant createdAt;
    private Instant updatedAt;

    public ResCareerExpectationDTO(CareerExpectation expectation) {
        this.id = expectation.getId();
        this.jobType = expectation.getJobType();
        this.jobTypeDisplay = expectation.getJobType().getDisplayName();
        this.desiredPosition = expectation.getDesiredPosition();
        this.desiredIndustry = expectation.getDesiredIndustry();
        this.desiredCity = expectation.getDesiredCity();
        this.minSalary = expectation.getMinSalary();
        this.maxSalary = expectation.getMaxSalary();
        this.salaryRange = formatSalaryRange(expectation.getMinSalary(), expectation.getMaxSalary());
        this.createdAt = expectation.getCreatedAt();
        this.updatedAt = expectation.getUpdatedAt();
    }

    private String formatSalaryRange(Double min, Double max) {
        if (min == null && max == null) return "Thỏa thuận";
        if (min == null) return "Dưới " + max.intValue() + " triệu";
        if (max == null) return "Trên " + min.intValue() + " triệu";
        if (min.equals(max)) return min.intValue() + " triệu";
        return min.intValue() + " - " + max.intValue() + " triệu";
    }
}