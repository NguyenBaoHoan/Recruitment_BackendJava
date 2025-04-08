package com.example.jobhunter.domain;

import java.time.Instant;

import com.example.jobhunter.util.error.SecurityUtil;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Table(name = "companies")
@Getter
@Setter
@Entity
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "name không được để trống")
    private String name;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;

    private String address;

    private String logo;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    private Instant updateAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    private Instant createAt;

    private String updateBy;

    private String createBy;

    @PrePersist
    public void handleCreateAt() {
        this.createBy = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        this.createAt = Instant.now();
    }

    @PreUpdate
    public void handleUpdateAt()
    {
        this.updateBy = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        this.updateAt = Instant.now();
    }

}
