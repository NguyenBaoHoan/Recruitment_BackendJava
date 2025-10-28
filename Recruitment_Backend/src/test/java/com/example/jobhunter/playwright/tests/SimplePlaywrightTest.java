package com.example.jobhunter.playwright.tests;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class SimplePlaywrightTest {
    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;

    @BeforeEach
    void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        context = browser.newContext();
        page = context.newPage();
    }

    @AfterEach
    void tearDown() {
        context.close();
        browser.close();
        playwright.close();
    }

    @Test
    @DisplayName("Simple test - Navigate to frontend and check login page contains 'Recruitment'")
    void simpleTest() {
        // Given
        page.navigate("http://localhost:5173/login");

        // Wait for page to load
        page.waitForLoadState();

        // ⏸️ DỪNG TẠI ĐÂY - Bạn có thể thao tác thủ công trên browser
        // Nhấn Resume (F8) trong Playwright Inspector để tiếp tục
        page.pause();

        // Then - Check for "Recruitment" visible on the page
        boolean jobHunterExists = page.locator("text=Recruitment").isVisible();

        assertTrue(jobHunterExists,
                "The text 'Recruitment' should appear somewhere on the login page");

        System.out.println("✅ Test passed! 'Recruitment' is visible on the login page.");

        // ⏸️ DỪNG LẠI ĐỂ XEM KẾT QUẢ - Nhấn Resume để đóng browser
        page.pause();
    }

    @Test
    @DisplayName("Test login page elements")
    void testLoginPageElements() {
        // Given
        page.navigate("http://localhost:5173/login");
        page.waitForLoadState();

        // Then - Check all main elements với slowMo sẽ thấy từng bước
        assertTrue(page.locator("text=Welcome Back").isVisible(), "Welcome Back title should be visible");
        assertTrue(page.locator("text=Sign in to Recruitment").isVisible(), "Subtitle should be visible");

        // 👇 Thêm actions để thấy slowMo hoạt động
        page.getByPlaceholder("your.email@example.com").click(); // Click vào email - thấy slowMo
        page.getByPlaceholder("your.email@example.com").fill("test@example.com"); // Type chậm - thấy slowMo

        page.getByPlaceholder("••••••••").click(); // Click vào password - thấy slowMo
        page.getByPlaceholder("••••••••").fill("password123"); // Type chậm - thấy slowMo

        // ⏸️ DỪNG LẠI ĐỂ XEM
        page.pause();

        System.out.println("✅ Login page elements test passed!");
    }

    @Test
    @DisplayName("Test Playwright is working")
    void testPlaywrightWorking() {
        // Given
        page.navigate("https://example.com");

        // Then
        assertTrue(page.title().contains("Example"));
        System.out.println("✅ Playwright is working! Page title: " + page.title());
    }

    @Test
    @DisplayName("Test Playwright can create page")
    void testPlaywrightCanCreatePage() {
        // Given
        Page newPage = context.newPage();

        // When
        newPage.navigate("https://httpbin.org/get");

        // Then
        assertTrue(newPage.content().contains("httpbin"));
        System.out.println("✅ Playwright can create and navigate pages!");

        newPage.close();
    }
}
