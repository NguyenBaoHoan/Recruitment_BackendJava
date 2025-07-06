package com.example.jobhunter.dto.response.job;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import com.example.jobhunter.util.constant.LevelEnum;

import lombok.Data;

@Data
public class ResCreateJobDTO {
    private long id;
    private String name;
    private String location;
    private double salary;
    private int quantity;
    private LevelEnum level;
    private String description;
    private Date startDate;
    private Date endDate;
    private boolean active;

    private List<String> skills;

    private Instant createdAt;
    private String createdBy;
}
