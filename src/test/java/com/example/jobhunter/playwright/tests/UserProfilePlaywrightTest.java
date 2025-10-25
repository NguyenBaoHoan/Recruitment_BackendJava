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

    @Test
    @DisplayName("Playwright Test 16: Cập nhật thông tin profile")
    void testUpdateProfile_Information() {
        // Given
        page.navigate(FRONTEND_URL + "/profile");
        page.waitForLoadState();

        // When
        page.fill("input[name='name']", "Updated User Name");
        page.fill("input[name='age']", "30");
        page.selectOption("select[name='gender']", "MALE");
        page.fill("textarea[name='address']", "Updated Address, Hà Nội");
        page.click("button[type='submit']");

        // Then
        page.waitForSelector(".success-notification");
        assertTrue(hasNotification("Cập nhật thông tin thành công"));

        // Verify updated information
        assertEquals("Updated User Name", page.inputValue("input[name='name']"));
        assertEquals("30", page.inputValue("input[name='age']"));
        assertEquals("Updated Address, Hà Nội", page.inputValue("textarea[name='address']"));

        System.out.println("✅ Update profile information test passed!");
    }

    @Test
    @DisplayName("Playwright Test 17: Đổi mật khẩu")
    void testChangePassword() {
        // Given
        page.navigate(FRONTEND_URL + "/profile");
        page.waitForLoadState();
        page.click("tab[data-testid='password-tab']");

        // When
        page.fill("input[name='oldPassword']", "password123");
        page.fill("input[name='newPassword']", "newPassword123");
        page.fill("input[name='confirmPassword']", "newPassword123");
        page.click("button[data-testid='change-password-btn']");

        // Then
        page.waitForSelector(".success-notification");
        assertTrue(hasNotification("Đổi mật khẩu thành công"));

        System.out.println("✅ Change password test passed!");
    }

    @Test
    @DisplayName("Playwright Test 18: Upload avatar")
    void testUploadAvatar() {
        // Given
        page.navigate(FRONTEND_URL + "/profile");
        page.waitForLoadState();

        // When
        // Note: Trong test thực tế, cần có file test image
        page.setInputFiles("input[type='file'][name='avatar']", 
                new java.nio.file.Path[] { java.nio.file.Paths.get("src/test/resources/test-avatar.jpg") });
        page.click("button[data-testid='upload-avatar-btn']");

        // Then
        page.waitForSelector(".success-notification");
        assertTrue(hasNotification("Cập nhật ảnh đại diện thành công"));

        // Verify avatar updated
        String avatarSrc = page.getAttribute(".user-avatar img", "src");
        assertNotNull(avatarSrc);
        assertTrue(avatarSrc.contains("uploads/"));

        System.out.println("✅ Upload avatar test passed!");
    }

    @Test
    @DisplayName("Playwright Test 19: Cập nhật career expectations")
    void testUpdateCareerExpectations() {
        // Given
        page.navigate(FRONTEND_URL + "/profile");
        page.waitForLoadState();
        page.click("tab[data-testid='career-tab']");

        // When
        page.selectOption("select[name='jobSeekingStatus']", "READY_NOW");
        page.fill("input[name='expectedSalary']", "2000");
        page.selectOption("select[name='preferredLocation']", "Hà Nội");
        page.selectOption("select[name='workType']", "FULL_TIME");
        page.click("button[data-testid='save-career-btn']");

        // Then
        page.waitForSelector(".success-notification");
        assertTrue(hasNotification("Cập nhật kỳ vọng nghề nghiệp thành công"));

        System.out.println("✅ Update career expectations test passed!");
    }

    @Test
    @DisplayName("Playwright Test 20: Cập nhật notification settings")
    void testUpdateNotificationSettings() {
        // Given
        page.navigate(FRONTEND_URL + "/profile");
        page.waitForLoadState();
        page.click("tab[data-testid='notifications-tab']");

        // When
        page.check("input[name='notifyNewMessages']");
        page.uncheck("input[name='notifyJobSuggestions']");
        page.check("input[name='notifyProfileUpdates']");
        page.click("button[data-testid='save-notifications-btn']");

        // Then
        page.waitForSelector(".success-notification");
        assertTrue(hasNotification("Cập nhật cài đặt thông báo thành công"));

        // Verify settings
        assertTrue(page.isChecked("input[name='notifyNewMessages']"));
        assertFalse(page.isChecked("input[name='notifyJobSuggestions']"));
        assertTrue(page.isChecked("input[name='notifyProfileUpdates']"));

        System.out.println("✅ Update notification settings test passed!");
    }
}