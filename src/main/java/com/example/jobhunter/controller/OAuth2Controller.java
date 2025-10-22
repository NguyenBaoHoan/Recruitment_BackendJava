package com.example.jobhunter.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jobhunter.domain.User;
import com.example.jobhunter.dto.request.OAuth2UserInfo;
import com.example.jobhunter.service.OAuth2Service;
import com.example.jobhunter.service.UserService;
import com.example.jobhunter.util.anotation.ApiMessage;
import com.example.jobhunter.util.error.SecurityUtil;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import com.example.jobhunter.dto.response.ResLoginDTO; // ✅ 1. Thêm import

/**
 * OAuth2 Authentication Controller
 * 
 * Endpoints:
 * 1. GET /api/v1/auth/oauth2/authorize/{provider} - Redirect to OAuth provider
 * 2. GET /api/v1/auth/oauth2/callback/{provider} - Handle OAuth callback
 * 
 * Flow:
 * Frontend → GET /oauth2/authorize/google
 * → Redirect to Google login
 * → User login & consent
 * → Google callback to /oauth2/callback/google?code=xxx
 * → Exchange code → user info
 * → Create/update user → Set cookie
 * → Redirect to frontend
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth/oauth2")
public class OAuth2Controller {
    private final OAuth2Service oauth2Service;
    private final UserService userService;
    private final SecurityUtil securityUtil;
    private final OAuth2AuthorizedClientService authorizedClientService;

    @Value("${hoan.oauth.frontend-redirect-url}")
    private String frontendRedirectUrl;

    /**
     * Endpoint 1: Initiate OAuth2 flow
     * 
     * Frontend gọi endpoint này để bắt đầu OAuth flow
     * Backend sẽ redirect browser đến provider login page
     * 
     * @param provider google | github | facebook
     * @param response HttpServletResponse để redirect
     * 
     *                 Example: GET /api/v1/auth/oauth2/authorize/google
     *                 → Redirect to
     *                 https://accounts.google.com/o/oauth2/v2/auth?...
     */
    @GetMapping("/authorize/{provider}")
    public void authorizeOAuth2(
            @PathVariable String provider,
            HttpServletResponse response) throws IOException {
        log.info("Initiating OAuth2 flow for provider: {}", provider);

        // Spring Security sẽ tự động handle redirect đến OAuth provider
        // Chỉ cần redirect đến endpoint của Spring Security
        String authorizationUrl = String.format("/oauth2/authorization/%s", provider);
        response.sendRedirect(authorizationUrl);
    }

    /**
     * Endpoint 2: Handle OAuth2 callback
     * 
     * Provider (Google/GitHub/Facebook) sẽ redirect về endpoint này
     * sau khi user đăng nhập & consent
     * 
     * Spring Security đã exchange code → access_token → user info
     * Chúng ta chỉ cần lấy user info và xử lý business logic
     * 
     * @param provider       Provider name
     * @param authentication OAuth2 authentication object
     * @param response       HttpServletResponse để set cookie
     * 
     *                       Flow:
     *                       1. Extract user info từ OAuth2User
     *                       2. Create/update user trong DB
     *                       3. Generate JWT token
     *                       4. Set httpOnly cookie
     *                       5. Redirect về frontend
     */
    @GetMapping("/callback/{provider}")
    @ApiMessage("OAuth2 login successful")
    public void handleOAuth2Callback(
            @PathVariable String provider,
            OAuth2AuthenticationToken authentication,
            HttpServletResponse response) throws IOException {
        try {
            log.info("Handling OAuth2 callback from provider: {}", provider);

            // Extract user info từ OAuth2User
            OAuth2User oauth2User = authentication.getPrincipal();
            Map<String, Object> attributes = oauth2User.getAttributes();

            log.debug("OAuth2 user attributes: {}", attributes);

            // Parse user info tùy theo provider
            OAuth2UserInfo oauth2UserInfo = oauth2Service.parseOAuth2User(provider, attributes);

            // Create hoặc update user trong DB
            User user = oauth2Service.processOAuth2Login(oauth2UserInfo);

            // Generate JWT tokens
            ResLoginDTO.UserLogin userLoginDTO = convertToUserLoginDTO(user);
            ResLoginDTO loginDTO = convertToResLoginDTO(user);
            String accessToken = securityUtil.createAccessToken(user.getEmail(), userLoginDTO);
            String refreshToken = securityUtil.createRefreshToken(user.getEmail(), loginDTO);

            // Update refresh token trong DB
            userService.updateUserToken(refreshToken, user.getEmail());

            // Set httpOnly cookie cho access_token
            ResponseCookie accessCookie = ResponseCookie
                    .from("access_token", accessToken)
                    .httpOnly(true)
                    .secure(true) // HTTPS only trong production
                    .path("/")
                    .maxAge(24 * 60 * 60) // 24 hours
                    .sameSite("Lax")
                    .build();

            // Set httpOnly cookie cho refresh_token
            ResponseCookie refreshCookie = ResponseCookie
                    .from("refresh_token", refreshToken)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(365 * 24 * 60 * 60) // 1 year
                    .sameSite("Lax")
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
            response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

            log.info("OAuth2 login successful for user: {}", user.getEmail());

            // Redirect về frontend với success message
            String redirectUrl = String.format(
                    "%s?oauth=success&provider=%s&email=%s",
                    frontendRedirectUrl,
                    provider,
                    URLEncoder.encode(user.getEmail(), StandardCharsets.UTF_8));

            response.sendRedirect(redirectUrl);

        } catch (Exception e) {
            log.error("OAuth2 callback error", e);

            // Redirect về frontend với error message
            String errorRedirectUrl = String.format(
                    "%s?oauth=error&message=%s",
                    frontendRedirectUrl.replace("/dashboard", "/login"),
                    URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));

            response.sendRedirect(errorRedirectUrl);
        }
    }

    /**
     * ✅ 4. Helper method: Convert User entity → UserLogin DTO
     * 
     * Method này convert User entity sang DTO để tạo JWT token
     * Tái sử dụng code từ AuthController.handleLogin()
     * 
     * @param user User entity
     * @return ResLoginDTO.UserLogin
     */
    private ResLoginDTO.UserLogin convertToUserLoginDTO(User user) {
        ResLoginDTO.UserLogin userLoginDTO = new ResLoginDTO.UserLogin();

        // Set các field cần thiết cho JWT
        userLoginDTO.setId(user.getId());
        userLoginDTO.setEmail(user.getEmail());
        userLoginDTO.setName(user.getName());

        return userLoginDTO;
    }

    private ResLoginDTO convertToResLoginDTO(User user) {
        ResLoginDTO userLoginDTO1 = new ResLoginDTO();
        ResLoginDTO.UserLogin userLoginDTO2 = new ResLoginDTO.UserLogin();

        // Set các field cần thiết cho JWT
        userLoginDTO2.setId(user.getId());
        userLoginDTO2.setEmail(user.getEmail());
        userLoginDTO2.setName(user.getName());

        userLoginDTO1.setUser(userLoginDTO2);

        return userLoginDTO1;
    }
}
