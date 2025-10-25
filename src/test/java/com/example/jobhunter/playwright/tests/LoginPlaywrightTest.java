package com.example.jobhunter.playwright.tests;

import com.example.jobhunter.playwright.base.BasePlaywrightTest;
import com.example.jobhunter.playwright.pages.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Login Page E2E Tests")
public class LoginPlaywrightTest extends BasePlaywrightTest {

    // 🎨 ANSI Color codes
    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String CYAN = "\u001B[36m";

    @Test
    @DisplayName("Playwright Test 1: Đăng nhập thành công với thông tin hợp lệ")
    void testLogin_Success() {
        System.out.println(CYAN + "🚀 Starting test: Login Success" + RESET);
        LoginPage loginPage = new LoginPage(page);

        // Given
        System.out.println(YELLOW + "📍 Step 1: Navigate to login page" + RESET);
        loginPage.navigate();

        // When
        System.out.println(YELLOW + "📍 Step 2: Enter credentials and submit" + RESET);
        loginPage.login("hoan@gmail.com", "123456");

        // Then
        System.out.println(YELLOW + "📍 Step 3: Verify redirect to dashboard" + RESET);
        page.waitForURL(FRONTEND_URL + "/dashboard");
        assertTrue(page.url().contains("/dashboard"));
        System.out.println(GREEN + "✅ Login successful test passed!" + RESET);
    }

    @Test
    @DisplayName("Playwright Test 2: Đăng nhập thất bại với email sai")
    void testLogin_InvalidEmail() {
        System.out.println(CYAN + "🚀 Starting test: Invalid Email" + RESET);
        LoginPage loginPage = new LoginPage(page);

        // Given
        System.out.println(YELLOW + "📍 Step 1: Navigate to login page" + RESET);
        loginPage.navigate();

        // When
        System.out.println(YELLOW + "📍 Step 2: Enter invalid credentials" + RESET);
        loginPage.login("invalid@example.com", "password123");

        // Then
        System.out.println(YELLOW + "📍 Step 3: Verify error message" + RESET);
        page.waitForSelector("div.bg-red-50");
        String errorText = page.textContent("div.bg-red-50");
        assertTrue(errorText.contains("Email hoặc mật khẩu không đúng"));
        System.out.println(GREEN + "✅ Invalid email test passed! Error: " + BLUE + errorText + RESET);
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