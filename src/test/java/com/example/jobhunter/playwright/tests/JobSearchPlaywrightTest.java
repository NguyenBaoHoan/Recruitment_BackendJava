package com.example.jobhunter.playwright.tests;

import com.example.jobhunter.playwright.base.BasePlaywrightTest;
import com.example.jobhunter.playwright.pages.JobPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Job Search E2E Tests")
public class JobSearchPlaywrightTest extends BasePlaywrightTest {

    private JobPage jobPage;

    @BeforeEach
    void setUpJobTests() {
        jobPage = new JobPage(page);
        // Đăng nhập trước khi test
        loginAsUser("user@example.com", "password123");
    }

    

    @Test
    @DisplayName("Playwright Test 9: Thêm job vào danh sách yêu thích")
    void testAddJobToFavorites() {
        // Given
        jobPage.navigateToAllJobs();
        jobPage.waitForJobsToLoad();
        
        // When
        jobPage.favoriteFirstJob();
        
        // Then
        page.waitForTimeout(1000); // Đợi animation
        assertTrue(jobPage.isFirstJobFavorited());
        
        // Kiểm tra trong trang saved jobs
        jobPage.navigateToSavedJobs();
        assertTrue(jobPage.hasJobs());
        System.out.println("✅ Add job to favorites test passed!");
    }

    @Test
    @DisplayName("Playwright Test 10: Phân trang danh sách job")
    void testJobPagination() {
        // Given
        jobPage.navigateToAllJobs();
        jobPage.waitForJobsToLoad();
        
        // When
        int firstPageJobCount = jobPage.getJobCount();
        String firstJobTitle = jobPage.getFirstJobTitle();
        
        jobPage.goToNextPage();
        
        // Then
        jobPage.waitForJobsToLoad();
        int secondPageJobCount = jobPage.getJobCount();
        String newFirstJobTitle = jobPage.getFirstJobTitle();
        
        // Kiểm tra job đầu tiên ở trang 2 khác với trang 1
        assertNotEquals(firstJobTitle, newFirstJobTitle);
        
        // Quay lại trang 1
        jobPage.goToPreviousPage();
        jobPage.waitForJobsToLoad();
        assertEquals(firstJobTitle, jobPage.getFirstJobTitle());
        
        System.out.println("✅ Job pagination test passed!");
    }
}