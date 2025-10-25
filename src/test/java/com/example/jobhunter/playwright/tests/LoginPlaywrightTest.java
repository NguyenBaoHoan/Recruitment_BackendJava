package com.example.jobhunter.playwright.tests;

import com.example.jobhunter.playwright.base.BasePlaywrightTest;
import com.example.jobhunter.playwright.pages.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Login Page E2E Tests")
public class LoginPlaywrightTest extends BasePlaywrightTest {

    @Test
    @DisplayName("Playwright Test 1: Đăng nhập thành công với thông tin hợp lệ")
    void testLogin_Success() {
        LoginPage loginPage = new LoginPage(page);
        
        // Given
        loginPage.navigate();
        
        // When
        loginPage.login("user@example.com", "password123");
        
        // Then
        page.waitForURL(FRONTEND_URL + "/dashboard");
        assertTrue(page.url().contains("/dashboard"));
        System.out.println("✅ Login successful test passed!");
    }

    @Test
    @DisplayName("Playwright Test 2: Đăng nhập thất bại với email sai")
    void testLogin_InvalidEmail() {
        LoginPage loginPage = new LoginPage(page);
        
        // Given
        loginPage.navigate();
        
        // When
        loginPage.login("invalid@example.com", "password123");
        
        // Then
        // Đợi error message xuất hiện
        page.waitForSelector(".error-message");
        assertTrue(loginPage.hasErrorMessage());
        assertTrue(loginPage.getErrorMessage().contains("Email hoặc mật khẩu không đúng"));
        System.out.println("✅ Invalid email test passed!");
    }

    @Test
    @DisplayName("Playwright Test 3: Kiểm tra validation form login")
    void testLogin_FormValidation() {
        LoginPage loginPage = new LoginPage(page);
        
        // Given
        loginPage.navigate();
        
        // When - Để trống email và password
        loginPage.fillEmail("");
        loginPage.fillPassword("");
        
        // Then
        assertFalse(loginPage.isLoginButtonEnabled());
        System.out.println("✅ Form validation test passed!");
    }

    @Test
    @DisplayName("Playwright Test 4: Chuyển đến trang register")
    void testNavigateToRegister() {
        LoginPage loginPage = new LoginPage(page);
        
        // Given
        loginPage.navigate();
        
        // When
        loginPage.clickRegister();
        
        // Then
        page.waitForURL(FRONTEND_URL + "/register");
        assertTrue(page.url().contains("/register"));
        System.out.println("✅ Navigate to register test passed!");
    }

    
}