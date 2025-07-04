package com.example.jobhunter.dto.response;

import java.time.Instant;

import com.example.jobhunter.util.constant.GenderEnum;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResCreateUserDTO {
    private long id;
    private String name;
    private String email;
    private String passWord;
    private int age;
    private GenderEnum gender;
    private String address;
    private Instant createdAt;
    private CompanyUser company;
    @Getter
    @Setter
    public static class CompanyUser {
        private long id;
        private String name;
    }
}
