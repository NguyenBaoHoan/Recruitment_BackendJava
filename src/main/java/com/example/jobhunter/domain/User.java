package com.example.jobhunter.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.example.jobhunter.util.constant.GenderEnum;
import com.example.jobhunter.util.error.SecurityUtil;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @NotBlank(message = "you cann't leave blank")
    private String email;

    private String password;

    private int age;
    private String role;
    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    private String address;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String refreshToken;

    private Instant createdAt;
    private Instant updateAt;
    private String createdBy;
    private String updatedBy;

    @Column(name = "cv_path")
    private String cvPath; // Đường dẫn đến CV của người dùng

    // Phần chat
    private String displayName;
    private String photoUrl;
    private String userType; // candidate, recruiter, admin

    // Oauth2
    /**
     * OAuth provider name(google, github, facebook)
     * Null nếu user đăng ký bằng email/password
     */
    private String oauthProvider;

    /**
     * User ID từ OAuth provider
     * Google: sub
     * GitHub: id
     * Facebook: id
     */
    private String oauthProviderId;

    // Các trường boolean để lưu cài đặt thông báo.
    @Column(name = "notify_new_messages", columnDefinition = "boolean default true")
    private boolean notifyNewMessages = true;

    @Column(name = "notify_profile_updates", columnDefinition = "boolean default true")
    private boolean notifyProfileUpdates = true;

    @Column(name = "notify_job_suggestions", columnDefinition = "boolean default false")
    private boolean notifyJobSuggestions = false;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToMany(mappedBy = "participants")
    @JsonIgnoreProperties(value = { "participants" })
    private List<ChatRoom> chatRooms = new ArrayList<>();

    // ✅ THÊM: Trạng thái tìm việc
    @Enumerated(EnumType.STRING)
    @Column(name = "job_seeking_status")
    @Builder.Default
    private JobSeekingStatus jobSeekingStatus = JobSeekingStatus.READY_NOW;

    // ✅ THÊM: Quan hệ với CareerExpectation
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = { "user" })
    @Builder.Default
    private List<CareerExpectation> careerExpectations = new ArrayList<>();

    @PrePersist
    public void handleCreateAt() {
        this.createdBy = SecurityUtil.getCurrentUserLogin().isPresent()
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        this.createdAt = Instant.now();
    }

    @PreUpdate
    public void handleUpdateAt() {
        this.updatedBy = SecurityUtil.getCurrentUserLogin().isPresent()
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        this.updateAt = Instant.now();
    }

    // Enum cho trạng thái tìm việc
    public enum JobSeekingStatus {
        READY_NOW("Sẵn sàng nhận việc ngay"),
        WITHIN_MONTH("Nhận việc trong tháng"),
        CONSIDERING_OPPORTUNITIES("Xem xét cơ hội mới"),
        NOT_SEEKING("Tạm thời chưa có nhu cầu");

        private final String displayName;

        JobSeekingStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
