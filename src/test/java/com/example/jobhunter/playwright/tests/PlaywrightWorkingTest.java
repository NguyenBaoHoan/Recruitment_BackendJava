package com.example.jobhunter.playwright.tests;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class PlaywrightWorkingTest {
    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;

    @BeforeEach
    void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
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
