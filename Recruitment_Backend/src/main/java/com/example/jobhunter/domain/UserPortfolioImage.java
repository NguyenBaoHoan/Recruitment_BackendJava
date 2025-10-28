package com.example.jobhunter.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "user_portfolio_image")
public class UserPortfolioImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="user_id", nullable = false)
    private Long userId;

    private String fileName;
    private String filePath;

    @Column(name="order_index")
    private Integer orderIndex;

    @Column(name="created_at")
    private Instant createdAt = Instant.now();

    // ===== Getters & Setters =====
    public Long getId() {
        return id;
    }
    public void setId(Long id) { // thường không cần set nhưng thêm cho đầy đủ
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }
    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
