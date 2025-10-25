package com.example.jobhunter.playwright.pages;

import com.microsoft.playwright.Page;

/**
 * Page Object Model cho Login Page
 */
public class LoginPage {
    private final Page page;
    
    // Selectors
    private static final String EMAIL_INPUT = "input[name='email']";
    private static final String PASSWORD_INPUT = "input[name='password']";
    private static final String LOGIN_BUTTON = "button[type='submit']";
    private static final String REGISTER_LINK = "a[href='/register']";
    private static final String GOOGLE_LOGIN_BUTTON = "button[data-testid='google-login']";
    private static final String ERROR_MESSAGE = ".error-message";

    public LoginPage(Page page) {
        this.page = page;
    }

    public void navigate() {
        page.navigate(System.getProperty("frontend.url", "http://localhost:5173") + "/login");
        page.waitForLoadState();
    }

    public void fillEmail(String email) {
        page.fill(EMAIL_INPUT, email);
    }

    public void fillPassword(String password) {
        page.fill(PASSWORD_INPUT, password);
    }

    public void clickLogin() {
        page.click(LOGIN_BUTTON);
    }

    public void clickRegister() {
        page.click(REGISTER_LINK);
    }

    public void clickGoogleLogin() {
        page.click(GOOGLE_LOGIN_BUTTON);
    }

    public void login(String email, String password) {
        fillEmail(email);
        fillPassword(password);
        clickLogin();
    }

    public boolean hasErrorMessage() {
        return page.locator(ERROR_MESSAGE).count() > 0;
    }

    public String getErrorMessage() {
        return page.textContent(ERROR_MESSAGE);
    }

    public boolean isLoginButtonEnabled() {
        return page.locator(LOGIN_BUTTON).isEnabled();
    }

    public String getTitle() {
        return page.title();
    }
}