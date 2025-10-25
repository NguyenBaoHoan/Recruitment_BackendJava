package com.example.jobhunter.playwright.tests;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class PlaywrightBasicTest {
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
    @DisplayName("Test Playwright basic functionality")
    void testPlaywrightBasic() {
        // Given
        page.navigate("https://www.google.com");

        // Then
        assertTrue(page.title().contains("Google"));
        System.out.println("✅ Playwright test passed! Page title: " + page.title());
    }

    @Test
    @DisplayName("Test Playwright element interaction")
    void testElementInteraction() {
        // Given
        page.navigate("https://www.google.com");

        // Wait for page to load
        page.waitForLoadState();

        // When
        page.fill("input[name='q']", "Playwright Java");

        // Then
        String value = page.inputValue("input[name='q']");
        assertEquals("Playwright Java", value);

        System.out.println("✅ Element interaction test passed!");
    }
}
