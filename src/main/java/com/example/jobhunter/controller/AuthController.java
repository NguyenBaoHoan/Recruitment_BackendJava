package com.example.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.jobhunter.domain.User;
import com.example.jobhunter.dto.request.ReqLoginDTO;
import com.example.jobhunter.dto.response.ResLoginDTO;
import com.example.jobhunter.service.UserService;
import com.example.jobhunter.util.anotation.ApiMessage;
import com.example.jobhunter.util.error.IdInvalidException;
import com.example.jobhunter.util.error.SecurityUtil;

import jakarta.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
        private final AuthenticationManagerBuilder authenticationManagerBuilder;
        private final SecurityUtil securityUtil;
        private UserService userService;

        @Value("${hoan.jwt.access-token-validity-in-seconds}")
        private long refreshTokenExpiration;

        public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil,
                        UserService userService) {
                this.authenticationManagerBuilder = authenticationManagerBuilder;
                this.securityUtil = securityUtil;
                this.userService = userService;
        }

        @PostMapping("/login")
        public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody ReqLoginDTO loginDTO) {
                // Nạp input gồm username/password vào Security
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                loginDTO.getUserName(), loginDTO.getPassWord());

                // xác thực người dùng => cần viết hàm loadUserByUsername
                Authentication authentication = authenticationManagerBuilder.getObject()
                                .authenticate(authenticationToken);

                // set thông tin người dùng dăng nhập vào context( có thể sử dụng sau này)
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // create token
                ResLoginDTO res = new ResLoginDTO();
                User currentUserDB = this.userService.handleGetUserByEmail(loginDTO.getUserName());
                if (currentUserDB != null) {

                        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(
                                        currentUserDB.getId(),
                                        currentUserDB.getEmail(),
                                        currentUserDB.getName());
                        res.setUser(userLogin);
                }
                // create access token
                String access_token = this.securityUtil.createAccessToken(authentication.getName(), res.getUser());
                res.setAccessToken(access_token);
                // create refresh token
                String refresh_token = this.securityUtil.createRefreshToken(loginDTO.getUserName(), res);
                // update user
                this.userService.updateUserToken(refresh_token, loginDTO.getUserName());
                ResponseCookie rescCookie = ResponseCookie
                                .from("refresh_token", refresh_token)
                                .httpOnly(true)
                                .secure(true)
                                .path("/")
                                .maxAge(refreshTokenExpiration)
                                .build();
                return ResponseEntity.ok()
                                .header("Set-Cookie", rescCookie.toString())
                                .body(res);
        }

        @GetMapping("/account")
        @ApiMessage("fetch account")
        public ResponseEntity<ResLoginDTO.UserLogin> getAccount() {
                String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get()
                                : "";

                User currentUserDB = this.userService.handleGetUserByEmail(email);
                ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin();
                if (currentUserDB != null) {
                        userLogin.setId(currentUserDB.getId());
                        userLogin.setEmail(currentUserDB.getEmail());
                        userLogin.setName(currentUserDB.getName());

                }

                return ResponseEntity.ok().body(userLogin);
        }

        @GetMapping("/refresh")
        public ResponseEntity<ResLoginDTO> getRefreshToken(
                        @CookieValue(name = "refresh_token", defaultValue = "abc") String refresh_token)
                        throws IdInvalidException {
                // check refresh token
                if (refresh_token.equals("abc")) {
                        throw new IdInvalidException("you don't have refresh token in cookie");
                }
                // check valid refresh token
                Jwt decodedToken = this.securityUtil.checkValidRefreshToken(refresh_token);
                String email = decodedToken.getSubject();
                // check user by token + email
                User currentUser = this.userService.getUserByRefreshTokenAndEmail(refresh_token, email);
                if (currentUser == null) {
                        throw new IdInvalidException("Refresh Token invalid");
                }

                // new token /set refresh token as cookies

                // create token
                ResLoginDTO res = new ResLoginDTO();
                User currentUserDB = this.userService.handleGetUserByEmail(email);
                if (currentUserDB != null) {

                        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(
                                        currentUserDB.getId(),
                                        currentUserDB.getEmail(),
                                        currentUserDB.getName());
                        res.setUser(userLogin);
                }
                // create access token
                String access_token = this.securityUtil.createAccessToken(email, res.getUser());

                res.setAccessToken(access_token);
                // create refresh token
                String new_refresh_token = this.securityUtil.createRefreshToken(email, res);
                // update user

                this.userService.updateUserToken(new_refresh_token, email);
                ResponseCookie rescCookie = ResponseCookie
                                .from("refresh_token", new_refresh_token)
                                .httpOnly(true)
                                .secure(true)
                                .path("/")
                                .maxAge(refreshTokenExpiration)
                                .build();
                return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, rescCookie.toString())
                                .body(res);
        }

        @PostMapping("/logout")
        public ResponseEntity<Void> logout() throws IdInvalidException {
                String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get()
                                : "";
                if (email.equals("")) {
                        throw new IdInvalidException("Access Token không hợp lệ");
                }
                // update refresh token = null
                this.userService.updateUserToken(null, email);

                // remove refresh token cookie
                ResponseCookie deleteSpringCookie = ResponseCookie
                                .from("refresh_token", null)
                                .httpOnly(true)
                                .secure(true)
                                .path("/")
                                .maxAge(0)
                                .build();

                return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, deleteSpringCookie.toString())
                                .body(null);
        }

}
