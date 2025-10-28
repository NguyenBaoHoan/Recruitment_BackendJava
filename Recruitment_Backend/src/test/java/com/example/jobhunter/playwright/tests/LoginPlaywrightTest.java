package com.example.jobhunter.playwright.tests;

import com.example.jobhunter.playwright.base.BasePlaywrightTest;
import com.example.jobhunter.playwright.pages.LoginPage;
import com.microsoft.playwright.Locator;
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

    // -----------------------------------------------------------------
    // Happy Path Test
    // -----------------------------------------------------------------

    @Test
    @DisplayName("DN01: Đăng nhập thành công với thông tin hợp lệ")
    void testLogin_Success() {
        System.out.println(CYAN + "🚀 Starting test: DN01 - Login Success" + RESET);
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
        assertTrue(page.url().contains("/dashboard"), "URL nên chứa /dashboard sau khi đăng nhập thành công");
        System.out.println(GREEN + "✅ Test Passed: Đăng nhập thành công!" + RESET);
    }

    // -----------------------------------------------------------------
    // Validation Tests (Client-side)
    // -----------------------------------------------------------------

    @Test
    @DisplayName("DN02: Thất bại khi để trống email")
    void testLogin_Fails_WhenEmailIsEmpty() {
        System.out.println(CYAN + "🚀 Starting test: DN02 - Empty Email" + RESET);
        LoginPage loginPage = new LoginPage(page);

        // Given
        System.out.println(YELLOW + "📍 Step 1: Navigate to login page" + RESET);
        loginPage.navigate();

        // When
        System.out.println(YELLOW + "📍 Step 2: Enter empty email and valid password" + RESET);
        loginPage.login("", "123456"); // Email trống

        // Then
        System.out.println(YELLOW + "📍 Step 3: Verify 'Email is required' error" + RESET);
        Locator errorLocator = page.getByText("Email is required");
        errorLocator.waitFor(); // Chờ cho element này xuất hiện
        String errorText = errorLocator.textContent();

        System.out.println(BLUE + "Validation Message: " + errorText + RESET);
        assertEquals("Email is required", errorText.trim(), "Nên hiển thị lỗi 'Email is required'");
        assertFalse(page.url().contains("/dashboard"), "Không nên chuyển trang khi email trống");
        System.out.println(GREEN + "✅ Test Passed: Hiển thị lỗi email trống." + RESET);
    }

    @Test
    @DisplayName("DN03: Thất bại khi để trống mật khẩu")
    void testLogin_Fails_WhenPasswordIsEmpty() {
        System.out.println(CYAN + "🚀 Starting test: DN03 - Empty Password" + RESET);
        LoginPage loginPage = new LoginPage(page);

        // Given
        System.out.println(YELLOW + "📍 Step 1: Navigate to login page" + RESET);
        loginPage.navigate();

        // When
        System.out.println(YELLOW + "📍 Step 2: Enter valid email and empty password" + RESET);
        loginPage.login("hoan@gmail.com", ""); // Mật khẩu trống

        // Then
        System.out.println(YELLOW + "📍 Step 3: Verify 'Password is required' error" + RESET);
        Locator errorLocator = page.getByText("Password is required");
        errorLocator.waitFor(); // Chờ cho element này xuất hiện
        String errorText = errorLocator.textContent();

        System.out.println(BLUE + "Validation Message: " + errorText + RESET);
        assertEquals("Password is required", errorText.trim(), "Nên hiển thị lỗi 'Password is required'");
        assertFalse(page.url().contains("/dashboard"), "Không nên chuyển trang khi mật khẩu trống");
        System.out.println(GREEN + "✅ Test Passed: Hiển thị lỗi mật khẩu trống." + RESET);
    }

    @Test
    @DisplayName("DN07/08: Thất bại khi email sai định dạng (HTML5 validation)")
    void testLogin_Fails_WhenEmailFormatIsInvalid() {
        System.out.println(CYAN + "🚀 Starting test: DN07/08 - Invalid Email Format" + RESET);
        LoginPage loginPage = new LoginPage(page);

        // Given
        System.out.println(YELLOW + "📍 Step 1: Navigate to login page" + RESET);
        loginPage.navigate();

        // When
        System.out.println(YELLOW + "📍 Step 2: Enter invalid email format and submit" + RESET);
        // Lưu ý: hàm login() có thể không hoạt động nếu nút submit bị vô hiệu hóa
        // Chúng ta điền thủ công và nhấp để kích hoạt validation
        page.fill("input[name='username']", "email-sai-dinh-dang");
        page.fill("input[name='password']", "123456");
        page.click("button[type='submit']");

        // Then
        System.out.println(YELLOW + "📍 Step 3: Verify browser validation error" + RESET);
        // Test case này kiểm tra thông báo validation popup của trình duyệt
        String validationMessage = page.locator("input[name='username']")
                .evaluate("el => el.validationMessage")
                .toString();

        System.out.println(BLUE + "Browser Validation Message: " + validationMessage + RESET);
        assertTrue(
                validationMessage.contains("Please include an '@'") || validationMessage.contains("Vui lòng bao gồm"),
                "Nên hiển thị thông báo lỗi định dạng email của trình duyệt");
        assertFalse(page.url().contains("/dashboard"), "Không nên chuyển trang khi email sai định dạng");
        System.out.println(GREEN + "✅ Test Passed: Hiển thị lỗi định dạng email." + RESET);
    }

    // -----------------------------------------------------------------
    // Authentication Tests (Server-side)
    // -----------------------------------------------------------------

    @Test
    @DisplayName("DN04/05/09: Thất bại với thông tin đăng nhập không chính xác")
    void testLogin_Fails_WhenCredentialsAreInvalid() {
        System.out.println(CYAN + "🚀 Starting test: DN04/05/09 - Invalid Credentials" + RESET);
        LoginPage loginPage = new LoginPage(page);

        // Given
        System.out.println(YELLOW + "📍 Step 1: Navigate to login page" + RESET);
        loginPage.navigate();

        // When
        System.out.println(YELLOW + "📍 Step 2: Enter invalid credentials and submit" + RESET);
        loginPage.login("invalid@example.com", "wrongpassword");

        // Then
        System.out.println(YELLOW + "📍 Step 3: Verify 'Email hoặc mật khẩu không đúng' error" + RESET);
        page.waitForSelector("div.bg-red-50"); // Chờ component lỗi chung
        String errorText = page.textContent("div.bg-red-50");

        System.out.println(BLUE + "Server Error: " + errorText + RESET);
        assertTrue(errorText.contains("Email hoặc mật khẩu không đúng"), "Nên hiển thị lỗi sai thông tin đăng nhập");
        assertFalse(page.url().contains("/dashboard"), "Không nên chuyển trang khi sai thông tin");
        System.out.println(GREEN + "✅ Test Passed: Hiển thị lỗi sai thông tin đăng nhập." + RESET);
    }

    // -----------------------------------------------------------------
    // Navigation Tests
    // -----------------------------------------------------------------

    @Test
    @DisplayName("Navigation: Chuyển hướng đến trang Register")
    void testNavigation_ToRegisterPage() {
        System.out.println(CYAN + "🚀 Starting test: Navigation to Register" + RESET);
        LoginPage loginPage = new LoginPage(page);

        // Given
        System.out.println(YELLOW + "📍 Step 1: Navigate to login page" + RESET);
        loginPage.navigate();

        // When
        System.out.println(YELLOW + "📍 Step 2: Click on 'Register' link" + RESET);
        loginPage.clickRegister();

        // Then
        System.out.println(YELLOW + "📍 Step 3: Verify redirect to register page" + RESET);
        page.waitForURL(FRONTEND_URL + "/register");
        assertTrue(page.url().contains("/register"), "URL nên chứa /register");
        System.out.println(GREEN + "✅ Test Passed: Chuyển trang Register thành công!" + RESET);
    }
}