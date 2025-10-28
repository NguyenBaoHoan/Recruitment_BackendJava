package com.example.jobhunter.dto;

import com.example.jobhunter.domain.UserPortfolioImage;
import lombok.Data;

@Data
public class PortfolioImageDTO {
    private Long id;
    private String fileName; // Tên gốc người dùng upload
    private String filePath; // Đường dẫn tương đối để FE ghép URL
    private Integer orderIndex;

    public static PortfolioImageDTO from(UserPortfolioImage e) {
        PortfolioImageDTO dto = new PortfolioImageDTO();
        dto.setId(e.getId());
        dto.setFileName(e.getFileName());
        dto.setFilePath(e.getFilePath());
        dto.setOrderIndex(e.getOrderIndex());
        return dto;
    }
}