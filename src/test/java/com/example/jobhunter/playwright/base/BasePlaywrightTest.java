package com.example.jobhunter.playwright.base;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

/**
 * Base class cho tất cả Playwright tests
 * Cung cấp setup và teardown chung
 */
public abstract class BasePlaywrightTest {
    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;

    // URLs từ system properties hoặc default values
    protected static final String FRONTEND_URL = System.getProperty("frontend.url", "http://localhost:5173");
    protected static final String BACKEND_URL = System.getProperty("backend.url", "http://localhost:8080");
    protected static final boolean HEADLESS = Boolean.parseBoolean(System.getProperty("playwright.headless", "true"));

    @BeforeEach
    void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(HEADLESS)
                .setSlowMo(5000)); // ✅ Chậm lại 5000ms mỗi action để dễ quan sát

        context = browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(1280, 720));

        page = context.newPage();

        // Set timeout mặc định
        page.setDefaultTimeout(30000); // 30 seconds
    }

    @AfterEach
    void tearDown() {
        if (page != null)
            page.close();
        if (context != null)
            context.close();
        if (browser != null)
            browser.close();
        if (playwright != null)
            playwright.close();
    }

    /**
     * Helper method để đăng nhập
     */
    protected void loginAsUser(String email, String password) {
        page.navigate(FRONTEND_URL + "/login");
        page.waitForLoadState();

        page.fill("input[name='email']", email);
        page.fill("input[name='password']", password);
        page.click("button[type='submit']");

        // Đợi redirect sau khi login thành công
        page.waitForURL(FRONTEND_URL + "/dashboard");
    }

    /**
     * Helper method để logout
     */
    protected void logout() {
        page.click("button[data-testid='logout-button']");
        page.waitForURL(FRONTEND_URL + "/login");
    }

    /**
     * Helper method để đợi element xuất hiện
     */
    protected void waitForElement(String selector) {
        page.waitForSelector(selector);
    }

    /**
     * Helper method để kiểm tra có alert/notification không
     */
    protected boolean hasNotification(String message) {
        try {
            return page.locator(".notification, .alert, .toast").textContent().contains(message);
        } catch (Exception e) {
            return false;
        }
    }
}