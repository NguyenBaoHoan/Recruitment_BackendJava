package com.example.jobhunter.service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.jobhunter.domain.User;
import com.example.jobhunter.dto.request.OAuth2UserInfo;
import com.example.jobhunter.repository.UserRepository;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service; // ✅ 1. Thêm import
import org.springframework.transaction.annotation.Transactional; // ✅ 2. Thêm import

/**
 * Service xử lý OAuth2 authentication
 * 
 * Flow:
 * 1. Extract user info từ OAuth2 provider
 * 2. Kiểm tra user đã tồn tại chưa (qua email)
 * 3. Nếu chưa: tạo user mới
 * 4. Nếu rồi: update thông tin (avatar, name...)
 * 5. Return user để tạo JWT token
 */
@Data
@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2Service {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Parse user info từ OAuth2 attributes
     * 
     * @param provider   Provider name (google, github, facebook)
     * @param attributes User attributes từ provider
     * @return Chuẩn hóa OAuth2UserInfo
     */
    public OAuth2UserInfo parseOAuth2User(String provider, Map<String, Object> attributes) {
        String normalized = provider == null ? "" : provider.toLowerCase();
        return switch (normalized) {
            case "google" -> OAuth2UserInfo.builder()
                    .provider("google")
                    .providerId((String) attributes.get("sub"))
                    .email((String) attributes.get("email"))
                    .name((String) attributes.getOrDefault("name", attributes.get("given_name")))
                    .avatarUrl((String) attributes.get("picture"))
                    .emailVerified((Boolean) attributes.getOrDefault("email_verified", Boolean.TRUE))
                    .build();
            case "github" -> {
                Object idObj = attributes.get("id");
                yield OAuth2UserInfo.builder()
                        .provider("github")
                        .providerId(idObj != null ? idObj.toString() : null)
                        .email((String) attributes.get("email"))
                        .name((String) attributes.getOrDefault("name", attributes.get("login")))
                        .avatarUrl((String) attributes.get("avatar_url"))
                        .emailVerified(Boolean.TRUE)
                        .build();
            }
            case "facebook" -> {
                @SuppressWarnings("unchecked")
                Map<String, Object> picture = (Map<String, Object>) attributes.get("picture");
                String avatarUrl = null;
                if (picture != null) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> data = (Map<String, Object>) picture.get("data");
                    if (data != null) {
                        avatarUrl = (String) data.get("url");
                    }
                }
                yield OAuth2UserInfo.builder()
                        .provider("facebook")
                        .providerId((String) attributes.get("id"))
                        .email((String) attributes.get("email"))
                        .name((String) attributes.get("name"))
                        .avatarUrl(avatarUrl)
                        .emailVerified(Boolean.TRUE)
                        .build();
            }
            default -> throw new IllegalArgumentException("Unsupported OAuth2 provider: " + provider);
        };
    }

    /**
     * Process OAuth2 login
     * 
     * Flow:
     * 1. Tìm user theo email
     * 2. Nếu tồn tại: update info (avatar, OAuth provider)
     * 3. Nếu chưa: tạo user mới với password random
     * 4. Return user để tạo JWT
     * 
     * @param oauth2User User info từ OAuth2 provider
     * @return User entity
     */
    @Transactional
    public User processOAuth2Login(OAuth2UserInfo oauth2User) {
        log.info("Processing OAuth2 login for email: {}, provider: {}",
                oauth2User.getEmail(), oauth2User.getProvider());

        // search email of user
        User existingUser = userRepository.findByEmail(oauth2User.getEmail());

        if (existingUser != null) {
            // if user exist => update info
            User user = existingUser;
            updateUserFromOAuth2(user, oauth2User);
            log.info("Updated existing user: {}", user.getEmail());
            return userRepository.save(user);
        } else {
            // User chưa tồn tại → tạo mới
            User newUser = createUserFromOAuth2(oauth2User);
            log.info("Created new user from OAuth2: {}", newUser.getEmail());
            return userRepository.save(newUser);
        }

    }

    /**
     * Update existing user với OAuth2 info
     */
    private void updateUserFromOAuth2(User user, OAuth2UserInfo oauth2User) {
        // Update name nếu chưa có
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(oauth2User.getName());
        }

        // Update avatar nếu có
        if (oauth2User.getAvatarUrl() != null) {
            // TODO: Download và lưu avatar vào server nếu muốn
            // user.setAvatar(oauth2User.getAvatarUrl());
        }

        // Set OAuth provider info
        user.setOauthProvider(oauth2User.getProvider());
        user.setOauthProviderId(oauth2User.getProviderId());
    }

    /**
     * Tạo user mới từ OAuth2 info
     */
    private User createUserFromOAuth2(OAuth2UserInfo oauth2User) {
        User newUser = new User();
        newUser.setEmail(oauth2User.getEmail());
        newUser.setName(oauth2User.getName());

        // Generate random password (user không biết, chỉ login qua OAuth)
        String randomPassword = UUID.randomUUID().toString();
        newUser.setPassword(passwordEncoder.encode(randomPassword));

        // Set OAuth provider info
        newUser.setOauthProvider(oauth2User.getProvider());
        newUser.setOauthProviderId(oauth2User.getProviderId());

        // Set default role
        // TODO: Set role từ database hoặc config
        // newUser.setRole(...);

        return newUser;
    }

}
