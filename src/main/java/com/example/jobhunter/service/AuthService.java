package com.example.jobhunter.service;

import com.example.jobhunter.domain.User;
import com.example.jobhunter.dto.response.ResLoginDTO;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class AuthService {
    private final UserService userService;
    private final GoogleIdTokenVerifier googleIdTokenVerifier;
    private final JwtEncoder jwtEncoder; // Sử dụng JwtEncoder có sẵn từ SecurityConfig của bạn

    public AuthService(
        UserService userService, 
        GoogleIdTokenVerifier googleIdTokenVerifier, 
        JwtEncoder jwtEncoder
    ) {
        this.userService = userService;
        this.googleIdTokenVerifier = googleIdTokenVerifier;
        this.jwtEncoder = jwtEncoder;
    }

    public ResLoginDTO loginWithGoogle(String googleToken) throws GeneralSecurityException, IOException {
        // 1. Xác thực Google Token
        GoogleIdToken idToken = googleIdTokenVerifier.verify(googleToken);
        if (idToken == null) {
            throw new IllegalArgumentException("Token Google không hợp lệ.");
        }

        // 2. Lấy thông tin và tìm hoặc tạo người dùng
        GoogleIdToken.Payload payload = idToken.getPayload();
        User user = this.userService.findOrCreateUserFromGoogle(payload);

        // 3. Tạo JWT Token của ứng dụng
        String appToken = this.createToken(user);
        
        // 4. Tạo đối tượng trả về
        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(user.getId(), user.getEmail(), user.getName());
        ResLoginDTO resLoginDTO = new ResLoginDTO();
        resLoginDTO.setAccessToken(appToken);
        resLoginDTO.setUser(userLogin);
        
        return resLoginDTO;
    }

    // Hàm tạo token sử dụng JwtEncoder có sẵn
    private String createToken(User user) {
        Instant now = Instant.now();
        // Bạn có thể thêm các claims khác nếu muốn, ví dụ như quyền (roles/permissions)
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("jobhunter-app")
                .issuedAt(now)
                .expiresAt(now.plus(24, ChronoUnit.HOURS)) // Hết hạn sau 24 giờ
                .subject(user.getEmail()) // Lấy email làm subject
                .claim("userId", user.getId())
                .claim("name", user.getName())
                .build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}