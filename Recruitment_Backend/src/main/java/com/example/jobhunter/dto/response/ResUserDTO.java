package com.example.jobhunter.dto.response;

import java.time.Instant;

import com.example.jobhunter.util.constant.GenderEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResUserDTO {
    private long id;
    private String name;
    private String email;
    private GenderEnum gender;
    private String address;
    private int age;
    private Instant updateAt;
    private Instant createdAt;
    private CompanyUser companyUser;

    @Data
    public static class CompanyUser {
        private long id;
        private String name;
    }

}
