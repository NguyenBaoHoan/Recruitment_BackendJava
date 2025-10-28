package com.example.jobhunter.dto.request;

import java.util.Date;
import java.util.List;

import com.example.jobhunter.util.constant.LevelEnum;

import lombok.Data;

@Data
public class ReqCreateJobDTO {
    // Thông tin cơ bản
    private String name; // Chức danh
    private String salary; // Mức lương
    private String location; // Địa điểm
    private Integer quantity; // Số lượng
    private String experience; // Kinh nghiệm
    private LevelEnum level; // Cấp bậc
    private String jobType; // Hình thức làm việc
    private LevelEnum educationLevel; // Trình độ học vấn
    private Date startDate; // Ngày bắt đầu
    private Date endDate; // Ngày kết thúc

    // Mô tả chi tiết
    private String description; // Mô tả công việc
    private String requirements; // Yêu cầu công việc
    private List<String> benefits; // Quyền lợi (1 dòng 1 ý)

    // Thông tin công ty
    private String companyName; // Tên công ty
    private String companyLogo; // Logo công ty
    private String workAddress; // Địa chỉ làm việc
    private String companyLocation; // Vị trí công ty
    private String companySize; // Quy mô công ty
    private String companyIndustry; // Ngành nghề công ty
    private String jobStatus; // Trạng thái công việc

    // Skills
    private List<Long> skillIds; // Danh sách ID của skills
}