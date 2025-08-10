package com.example.jobhunter.service;

// <<< THÊM MỚI >>> Các import cần thiết cho đăng nhập bằng email/password
import com.example.jobhunter.domain.User;
import com.example.jobhunter.dto.request.ReqRegisterDTO;
import com.example.jobhunter.dto.response.ResLoginDTO;
import com.example.jobhunter.util.error.IdInvalidException;
import com.example.jobhunter.util.error.SecurityUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
// <<< KẾT THÚC THÊM MỚI >>>

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Service
public class AuthService {
    private final UserService userService;
    private final GoogleIdTokenVerifier googleIdTokenVerifier;
    private final SecurityUtil securityUtil; // <<< SỬA ĐỔI >>> Dùng SecurityUtil để tạo token
    
    // <<< THÊM MỚI >>> Các thành phần cho đăng nhập email/password
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;


    // <<< SỬA ĐỔI >>> Cập nhật constructor
    public AuthService(
            UserService userService,
            GoogleIdTokenVerifier googleIdTokenVerifier,
            SecurityUtil securityUtil,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.googleIdTokenVerifier = googleIdTokenVerifier;
        this.securityUtil = securityUtil;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }
    
    // <<< THÊM MỚI >>> Logic đăng ký tài khoản mới
    public User register(ReqRegisterDTO reqRegisterDTO) throws IdInvalidException {
        // Kiểm tra email đã tồn tại chưa
        if (this.userService.isEmailExist(reqRegisterDTO.getEmail())) {
            throw new IdInvalidException("Email đã tồn tại. Vui lòng sử dụng email khác.");
        }
        
        // Mã hóa mật khẩu
        String hashedPassword = this.passwordEncoder.encode(reqRegisterDTO.getPassword());
        
        // Tạo user mới và lưu vào DB
        User newUser = new User();
        newUser.setName(reqRegisterDTO.getName());
        newUser.setEmail(reqRegisterDTO.getEmail());
        newUser.setPassWord(hashedPassword);
        
        return this.userService.handleSaveUser(newUser);
    }
    
    // <<< THÊM MỚI >>> Logic đăng nhập bằng email/password
    public ResLoginDTO login(String username, String password) {
        // 1. Xác thực người dùng
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 2. Lấy thông tin người dùng
        User currentUser = this.userService.handleGetUserByEmail(username);
        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(
            currentUser.getId(),
            currentUser.getEmail(),
            currentUser.getName()
        );

        // 3. Tạo token và response
        ResLoginDTO res = new ResLoginDTO();
        res.setUser(userLogin);
        String accessToken = this.securityUtil.createAccessToken(authentication.getName(), res.getUser());
        res.setAccessToken(accessToken);
        
        // Cập nhật refresh token cho người dùng
        String refreshToken = this.securityUtil.createRefreshToken(username, res);
        this.userService.updateUserToken(refreshToken, username);

        return res;
    }


    // <<< GIỮ NGUYÊN >>> Logic đăng nhập bằng Google
    public ResLoginDTO loginWithGoogle(String googleToken) throws GeneralSecurityException, IOException {
        GoogleIdToken idToken = googleIdTokenVerifier.verify(googleToken);
        if (idToken == null) {
            throw new IllegalArgumentException("Token Google không hợp lệ.");
        }

        GoogleIdToken.Payload payload = idToken.getPayload();
        User userInDb = this.userService.findOrCreateUserFromGoogle(payload);
        
        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(
            userInDb.getId(),
            userInDb.getEmail(),
            userInDb.getName()
        );

        ResLoginDTO res = new ResLoginDTO();
        res.setUser(userLogin);

        String accessToken = this.securityUtil.createAccessToken(userInDb.getEmail(), res.getUser());
        res.setAccessToken(accessToken);

        String refreshToken = this.securityUtil.createRefreshToken(userInDb.getEmail(), res);
        this.userService.updateUserToken(refreshToken, userInDb.getEmail());
        
        return res;
    }
}