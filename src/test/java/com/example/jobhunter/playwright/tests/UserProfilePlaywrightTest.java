package com.example.jobhunter.playwright.tests;

import com.example.jobhunter.playwright.base.BasePlaywrightTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User Profile E2E Tests")
public class UserProfilePlaywrightTest extends BasePlaywrightTest {

    @BeforeEach
    void setUpProfile() {
        // Đăng nhập trước khi test profile
        loginAsUser("user@example.com", "password123");
    }



}