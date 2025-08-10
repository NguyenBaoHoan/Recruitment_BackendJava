package com.example.jobhunter.dto.request;

import com.example.jobhunter.domain.CareerExpectation;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqCareerExpectationDTO {
    
    @NotNull(message = "Loại hình công việc không được để trống")
    private CareerExpectation.JobType jobType;

    @NotBlank(message = "Vị trí mong muốn không được để trống")
    @Size(max = 255, message = "Vị trí mong muốn không được vượt quá 255 ký tự")
    private String desiredPosition;

    @NotBlank(message = "Ngành nghề mong muốn không được để trống")
    @Size(max = 255, message = "Ngành nghề mong muốn không được vượt quá 255 ký tự")
    private String desiredIndustry;

    @NotBlank(message = "Thành phố mong muốn không được để trống")
    @Size(max = 255, message = "Thành phố mong muốn không được vượt quá 255 ký tự")
    private String desiredCity;

    @NotNull(message = "Mức lương tối thiểu không được để trống")
    @DecimalMin(value = "0.0", message = "Mức lương tối thiểu phải lớn hơn 0")
    private Double minSalary;

    @NotNull(message = "Mức lương tối đa không được để trống")
    @DecimalMin(value = "0.0", message = "Mức lương tối đa phải lớn hơn 0")
    private Double maxSalary;

    // Validation custom
    @AssertTrue(message = "Mức lương tối đa phải lớn hơn hoặc bằng mức lương tối thiểu")
    public boolean isSalaryRangeValid() {
        return maxSalary == null || minSalary == null || maxSalary >= minSalary;
    }
}