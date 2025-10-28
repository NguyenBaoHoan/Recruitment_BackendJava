package com.example.jobhunter.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2UserInfo {
    private String providerId;
    private String provider;
    private String email;
    private String name;
    private String avatarUrl;
    private Boolean emailVerified;
}
