package com.example.jobhunter.domain;

import java.time.Instant;

import com.example.jobhunter.util.constant.GenderEnum;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    @NotBlank(message = "you cann't leave blank")
    private String email;
    @NotBlank(message = "you cann't leave blank")
    private String passWord;

    private int age;

    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    private String address;
    private String refreshToken;
    private Instant createdAt;
    private Instant updateAt;
    private String createdBy;
    private String updatedBy;
}
