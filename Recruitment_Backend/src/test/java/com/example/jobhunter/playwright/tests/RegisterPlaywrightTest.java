package com.example.jobhunter.playwright.tests;

import com.example.jobhunter.playwright.base.BasePlaywrightTest;
import com.example.jobhunter.playwright.pages.RegisterPage;
import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Register Page E2E Tests
 * 
 * Test Coverage:
 * - Happy Path: Đăng ký thành công
 * - Validation Tests: Kiểm tra validation form (client-side)
 * - Server Error Tests: Kiểm tra lỗi từ server (email đã tồn tại...)
 * - Navigation Tests: Kiểm tra chuyển hướng
 * 
 * Format: Dựa theo LoginPlaywrightTest với màu sắc và log chi tiết
 */
@DisplayName("Register Page E2E Tests")
public class RegisterPlaywrightTest extends BasePlaywrightTest {

    // ========================================
    // ANSI COLOR CODES - Để console đẹp hơn
    // ========================================

    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String CYAN = "\u001B[36m";
    private static final String RED = "\u001B[31m";

    // ========================================
    // HAPPY PATH TEST - Đăng ký thành công
    // ========================================

    @Test
    @DisplayName("DK01: Đăng ký thành công với thông tin hợp lệ")
    void testRegister_Success() {
        System.out.println(CYAN + "🚀 Starting test: DK01 - Register Success" + RESET);
        RegisterPage registerPage = new RegisterPage(page);

        // Given
        System.out.println(YELLOW + "📍 Step 1: Navigate to register page" + RESET);
        registerPage.navigate();

        // When
        System.out.println(YELLOW + "📍 Step 2: Fill form with valid data and submit" + RESET);
        String testEmail = "newuser_" + System.currentTimeMillis() + "@example.com";
        registerPage.registerQuick("Nguyễn Văn A", testEmail, "Password123");

        // Then
        System.out.println(YELLOW + "📍 Step 3: Verify redirect to login page" + RESET);
        page.waitForURL(FRONTEND_URL + "/login");
        assertTrue(page.url().contains("/login"), "URL nên chứa /login sau khi đăng ký thành công");
        System.out.println(GREEN + "✅ Test Passed: Đăng ký thành công và redirect đến login!" + RESET);
    }

    // ========================================
    // VALIDATION TESTS - Kiểm tra validation client-side
    // ========================================

    @Test
    @DisplayName("DK02: Thất bại khi để trống tên")
    void testRegister_Fails_WhenNameIsEmpty() {
        System.out.println(CYAN + "🚀 Starting test: DK02 - Empty Name" + RESET);
        RegisterPage registerPage = new RegisterPage(page);

        // Given
        System.out.println(YELLOW + "📍 Step 1: Navigate to register page" + RESET);
        registerPage.navigate();

        // When
        System.out.println(YELLOW + "📍 Step 2: Fill form with empty name" + RESET);
        registerPage.fillName(""); // Tên trống
        registerPage.fillEmail("test@example.com");
        registerPage.fillPassword("Password123");
        registerPage.fillConfirmPassword("Password123");
        registerPage.checkTermsCheckbox();
        registerPage.clickRegister();

        // Then
        System.out.println(YELLOW + "📍 Step 3: Verify 'Name is required' error" + RESET);
        Locator errorLocator = page.getByText("Name is required");
        errorLocator.waitFor();
        String errorText = errorLocator.textContent();

        System.out.println(BLUE + "Validation Message: " + errorText + RESET);
        assertEquals("Name is required", errorText.trim(), "Nên hiển thị lỗi 'Name is required'");
        assertFalse(page.url().contains("/login"), "Không nên redirect khi tên trống");
        System.out.println(GREEN + "✅ Test Passed: Hiển thị lỗi tên trống." + RESET);
    }

    @Test
    @DisplayName("DK03: Thất bại khi để trống email")
    void testRegister_Fails_WhenEmailIsEmpty() {
        System.out.println(CYAN + "🚀 Starting test: DK03 - Empty Email" + RESET);
        RegisterPage registerPage = new RegisterPage(page);

        // Given
        System.out.println(YELLOW + "📍 Step 1: Navigate to register page" + RESET);
        registerPage.navigate();

        // When
        System.out.println(YELLOW + "📍 Step 2: Fill form with empty email" + RESET);
        registerPage.fillName("Nguyễn Văn A");
        registerPage.fillEmail(""); // Email trống
        registerPage.fillPassword("Password123");
        registerPage.fillConfirmPassword("Password123");
        registerPage.checkTermsCheckbox();
        registerPage.clickRegister();

        // Then
        System.out.println(YELLOW + "📍 Step 3: Verify 'Email is required' error" + RESET);
        Locator errorLocator = page.getByText("Email is required");
        errorLocator.waitFor();
        String errorText = errorLocator.textContent();

        System.out.println(BLUE + "Validation Message: " + errorText + RESET);
        assertEquals("Email is required", errorText.trim(), "Nên hiển thị lỗi 'Email is required'");
        assertFalse(page.url().contains("/login"), "Không nên redirect khi email trống");
        System.out.println(GREEN + "✅ Test Passed: Hiển thị lỗi email trống." + RESET);
    }

    @Test
    @DisplayName("DK04: Thất bại khi để trống password")
    void testRegister_Fails_WhenPasswordIsEmpty() {
        System.out.println(CYAN + "🚀 Starting test: DK04 - Empty Password" + RESET);
        RegisterPage registerPage = new RegisterPage(page);

        // Given
        System.out.println(YELLOW + "📍 Step 1: Navigate to register page" + RESET);
        registerPage.navigate();

        // When
        System.out.println(YELLOW + "📍 Step 2: Fill form with empty password" + RESET);
        registerPage.fillName("Nguyễn Văn A");
        registerPage.fillEmail("test@example.com");
        registerPage.fillPassword(""); // Password trống
        registerPage.fillConfirmPassword("");
        registerPage.checkTermsCheckbox();
        registerPage.clickRegister();

        // Then
        System.out.println(YELLOW + "📍 Step 3: Verify 'Password is required' error" + RESET);
        Locator errorLocator = page.getByText("Password is required");
        errorLocator.waitFor();
        String errorText = errorLocator.textContent();

        System.out.println(BLUE + "Validation Message: " + errorText + RESET);
        assertEquals("Password is required", errorText.trim(), "Nên hiển thị lỗi 'Password is required'");
        assertFalse(page.url().contains("/login"), "Không nên redirect khi password trống");
        System.out.println(GREEN + "✅ Test Passed: Hiển thị lỗi password trống." + RESET);
    }

    @Test
    @DisplayName("DK06: Thất bại khi password không khớp")
    void testRegister_Fails_WhenPasswordsDoNotMatch() {
        System.out.println(CYAN + "🚀 Starting test: DK06 - Passwords Do Not Match" + RESET);
        RegisterPage registerPage = new RegisterPage(page);

        // Given
        System.out.println(YELLOW + "📍 Step 1: Navigate to register page" + RESET);
        registerPage.navigate();

        // When
        System.out.println(YELLOW + "📍 Step 2: Fill form with mismatched passwords" + RESET);
        registerPage.fillName("Nguyễn Văn A");
        registerPage.fillEmail("test@example.com");
        registerPage.fillPassword("Password123");
        registerPage.fillConfirmPassword("Password456"); // Không khớp
        registerPage.checkTermsCheckbox();
        registerPage.clickRegister();

        // Then
        System.out.println(YELLOW + "📍 Step 3: Verify 'Passwords do not match' error" + RESET);
        Locator errorLocator = page.getByText("Passwords do not match");
        errorLocator.waitFor();
        String errorText = errorLocator.textContent();

        System.out.println(BLUE + "Validation Message: " + errorText + RESET);
        assertEquals("Passwords do not match", errorText.trim(),
                "Nên hiển thị lỗi 'Passwords do not match'");
        assertFalse(page.url().contains("/login"), "Không nên redirect khi password không khớp");
        System.out.println(GREEN + "✅ Test Passed: Hiển thị lỗi password không khớp." + RESET);
    }

    @Test
    @DisplayName("DK07: Thất bại khi email sai định dạng")
    void testRegister_Fails_WhenEmailFormatIsInvalid() {
        System.out.println(CYAN + "🚀 Starting test: DK07 - Invalid Email Format" + RESET);
        RegisterPage registerPage = new RegisterPage(page);

        // Given
        System.out.println(YELLOW + "📍 Step 1: Navigate to register page" + RESET);
        registerPage.navigate();

        // When
        System.out.println(YELLOW + "📍 Step 2: Fill form with invalid email format" + RESET);
        registerPage.fillName("Nguyễn Văn A");
        page.fill("input[name='email']", "email-sai-dinh-dang"); // Email sai định dạng
        registerPage.fillPassword("Password123");
        registerPage.fillConfirmPassword("Password123");
        registerPage.checkTermsCheckbox();
        registerPage.clickRegister();

        // Then
        System.out.println(YELLOW + "📍 Step 3: Verify browser validation error" + RESET);
        String validationMessage = page.locator("input[name='email']")
                .evaluate("el => el.validationMessage")
                .toString();

        System.out.println(BLUE + "Browser Validation Message: " + validationMessage + RESET);
        assertTrue(
                validationMessage.contains("Please include an '@'") ||
                        validationMessage.contains("Vui lòng bao gồm"),
                "Nên hiển thị thông báo lỗi định dạng email");
        assertFalse(page.url().contains("/login"), "Không nên redirect khi email sai định dạng");
        System.out.println(GREEN + "✅ Test Passed: Hiển thị lỗi định dạng email." + RESET);
    }

    @Test
    @DisplayName("DK08: Thất bại khi tên chỉ chứa số")
    void testRegister_Fails_WhenNameContainsOnlyNumbers() {
        System.out.println(CYAN + "🚀 Starting test: DK08 - Name Contains Only Numbers" + RESET);
        RegisterPage registerPage = new RegisterPage(page);

        // Given
        System.out.println(YELLOW + "📍 Step 1: Navigate to register page" + RESET);
        registerPage.navigate();

        // When
        System.out.println(YELLOW + "📍 Step 2: Fill form with numeric name" + RESET);
        registerPage.fillName("12345"); // Tên chỉ có số
        registerPage.fillEmail("test@example.com");
        registerPage.fillPassword("Password123");
        registerPage.fillConfirmPassword("Password123");
        registerPage.checkTermsCheckbox();
        registerPage.clickRegister();

        // Then
        System.out.println(YELLOW + "📍 Step 3: Verify 'Name should only contain letters' error" + RESET);
        Locator errorLocator = page.getByText("Name should only contain letters");
        errorLocator.waitFor();
        String errorText = errorLocator.textContent();

        System.out.println(BLUE + "Validation Message: " + errorText + RESET);
        assertEquals("Name should only contain letters", errorText.trim(),
                "Nên hiển thị lỗi 'Name should only contain letters'");
        assertFalse(page.url().contains("/login"), "Không nên redirect khi tên không hợp lệ");
        System.out.println(GREEN + "✅ Test Passed: Hiển thị lỗi tên chỉ có số." + RESET);
    }

    // ========================================
    // NAVIGATION TESTS - Kiểm tra chuyển hướng
    // ========================================

    @Test
    @DisplayName("Navigation: Chuyển hướng đến trang Login")
    void testNavigation_ToLoginPage() {
        System.out.println(CYAN + "🚀 Starting test: Navigation - To Login Page" + RESET);
        RegisterPage registerPage = new RegisterPage(page);

        // Given
        System.out.println(YELLOW + "📍 Step 1: Navigate to register page" + RESET);
        registerPage.navigate();

        // When
        System.out.println(YELLOW + "📍 Step 2: Click login link" + RESET);
        registerPage.clickLoginLink();

        // Then
        System.out.println(YELLOW + "📍 Step 3: Verify redirect to login page" + RESET);
        page.waitForURL(FRONTEND_URL + "/login");
        assertTrue(page.url().contains("/login"), "URL nên chứa /login");
        System.out.println(GREEN + "✅ Test Passed: Navigate to login page successfully!" + RESET);
    }

}
