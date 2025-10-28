package com.example.jobhunter.dto.request;

import com.example.jobhunter.domain.User;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqJobSeekingStatusDTO {
    @NotNull(message = "Trạng thái tìm việc không được để trống")
    private User.JobSeekingStatus jobSeekingStatus;
}