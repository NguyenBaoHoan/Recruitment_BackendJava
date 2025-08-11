package com.example.jobhunter.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqGoogleLoginDTO {
    @NotBlank(message = "Google token cannot be empty")
    private String token;
}