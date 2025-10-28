package com.example.jobhunter.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "user_portfolio")
public class UserPortfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="user_id", unique = true, nullable = false)
    private Long userId;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name="created_at")
    private Instant createdAt = Instant.now();

    @Column(name="updated_at")
    private Instant updatedAt = Instant.now();

    @PreUpdate
    public void preUpdate() {
        updatedAt = Instant.now();
    }

    // ====== Getters & Setters ======

    public Long getId() {
        return id;
    }

    public void setId(Long id) { // (thường không cần set, nhưng để đủ cặp)
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) { // dùng trong upsertDescription()
        this.userId = userId;
    }

    public String getDescription() { // dùng trong controller trả về DTO
        return description;
    }

    public void setDescription(String description) { // dùng trong upsertDescription()
        this.description = description;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) { // hiếm khi cần nhưng thêm cho đầy đủ
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) { // updated qua @PreUpdate, vẫn để đủ cặp
        this.updatedAt = updatedAt;
    }
}
