package com.example.jobhunter.playwright.tests;

import com.example.jobhunter.playwright.base.BasePlaywrightTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Dashboard E2E Tests")
public class DashboardPlaywrightTest extends BasePlaywrightTest {

    @BeforeEach
    void setUpDashboard() {
        // Đăng nhập trước khi test dashboard
        loginAsUser("user@example.com", "password123");
    }

    @Test
    @DisplayName("Playwright Test 11: Kiểm tra dashboard load thành công")
    void testDashboard_LoadSuccessfully() {
        // Given & When
        page.navigate(FRONTEND_URL + "/dashboard");
        page.waitForLoadState();

        // Then
        assertTrue(page.url().contains("/dashboard"));
        assertTrue(page.locator("h1").textContent().contains("Dashboard"));
        assertTrue(page.locator(".user-welcome").isVisible());
        System.out.println("✅ Dashboard load test passed!");
    }

    @Test
    @DisplayName("Playwright Test 12: Kiểm tra sidebar navigation")
    void testSidebar_Navigation() {
        // Given
        page.navigate(FRONTEND_URL + "/dashboard");
        page.waitForLoadState();

        // When & Then - Test các menu items
        page.click("a[href='/jobs']");
        page.waitForURL(FRONTEND_URL + "/jobs");
        assertTrue(page.url().contains("/jobs"));

        page.click("a[href='/jobs/saved']");
        page.waitForURL(FRONTEND_URL + "/jobs/saved");
        assertTrue(page.url().contains("/jobs/saved"));

        page.click("a[href='/profile']");
        page.waitForURL(FRONTEND_URL + "/profile");
        assertTrue(page.url().contains("/profile"));

        System.out.println("✅ Sidebar navigation test passed!");
    }

    @Test
    @DisplayName("Playwright Test 13: Kiểm tra user profile dropdown")
    void testUserProfile_Dropdown() {
        // Given
        page.navigate(FRONTEND_URL + "/dashboard");
        page.waitForLoadState();

        // When
        page.click(".user-avatar");
        page.waitForSelector(".dropdown-menu");

        // Then
        assertTrue(page.locator(".dropdown-menu").isVisible());
        assertTrue(page.locator("a[href='/profile']").isVisible());
        assertTrue(page.locator("button[data-testid='logout-button']").isVisible());

        System.out.println("✅ User profile dropdown test passed!");
    }

    @Test
    @DisplayName("Playwright Test 14: Kiểm tra logout functionality")
    void testLogout_Functionality() {
        // Given
        page.navigate(FRONTEND_URL + "/dashboard");
        page.waitForLoadState();

        // When
        logout();

        // Then
        page.waitForURL(FRONTEND_URL + "/login");
        assertTrue(page.url().contains("/login"));
        assertTrue(page.locator("form[data-testid='login-form']").isVisible());

        System.out.println("✅ Logout functionality test passed!");
    }

    @Test
    @DisplayName("Playwright Test 15: Kiểm tra dashboard stats/widgets")
    void testDashboard_StatsWidgets() {
        // Given
        page.navigate(FRONTEND_URL + "/dashboard");
        page.waitForLoadState();

        // Then
        assertTrue(page.locator(".stats-widget").count() > 0);
        assertTrue(page.locator(".job-stats").isVisible());
        assertTrue(page.locator(".application-stats").isVisible());
        assertTrue(page.locator(".recent-activities").isVisible());

        // Kiểm tra có số liệu hiển thị
        String jobCount = page.textContent(".job-count");
        assertNotNull(jobCount);
        assertFalse(jobCount.trim().isEmpty());

        System.out.println("✅ Dashboard stats widgets test passed!");
    }
}