package com.example.jobhunter.playwright.pages;

import com.microsoft.playwright.Page;

/**
 * Page Object Model cho Register Page
 * Chứa các selector và action methods cho trang đăng ký
 */
public class RegisterPage {
    private final Page page;

    // ========================================
    // SELECTORS - Định nghĩa các selector
    // ========================================

    // Input fields
    private static final String NAME_INPUT = "input[name='name']";
    private static final String EMAIL_INPUT = "input[name='email']";
    private static final String PASSWORD_INPUT = "input[name='password']";
    private static final String CONFIRM_PASSWORD_INPUT = "input[name='confirmPassword']";

    // Buttons
    private static final String REGISTER_BUTTON = "button[type='submit']";
    private static final String LOGIN_LINK = "a[href='/login']";

    // Validation & Error messages
    private static final String ERROR_MESSAGE_BOX = "div.bg-red-50";

    // Terms checkbox
    private static final String TERMS_CHECKBOX = "input[type='checkbox'][required]";

    // ========================================
    // CONSTRUCTOR
    // ========================================

    public RegisterPage(Page page) {
        this.page = page;
    }

    // ========================================
    // NAVIGATION METHODS
    // ========================================

    /**
     * Navigate đến trang Register
     */
    public void navigate() {
        page.navigate(System.getProperty("frontend.url", "http://localhost:5173") + "/register");
        page.waitForLoadState();
    }

    // ========================================
    // INPUT METHODS - Điền thông tin vào form
    // ========================================

    /**
     * Điền tên đầy đủ
     */
    public void fillName(String name) {
        page.fill(NAME_INPUT, name);
    }

    /**
     * Điền email
     */
    public void fillEmail(String email) {
        page.fill(EMAIL_INPUT, email);
    }

    /**
     * Điền mật khẩu
     */
    public void fillPassword(String password) {
        page.fill(PASSWORD_INPUT, password);
    }

    /**
     * Điền xác nhận mật khẩu
     */
    public void fillConfirmPassword(String confirmPassword) {
        page.fill(CONFIRM_PASSWORD_INPUT, confirmPassword);
    }

    /**
     * Check vào checkbox điều khoản
     */
    public void checkTermsCheckbox() {
        page.check(TERMS_CHECKBOX);
    }

    // ========================================
    // ACTION METHODS - Thao tác với form
    // ========================================

    /**
     * Click nút Register
     */
    public void clickRegister() {
        page.click(REGISTER_BUTTON);
    }

    /**
     * Click link đến Login page
     */
    public void clickLoginLink() {
        page.click(LOGIN_LINK);
    }

    /**
     * Đăng ký với đầy đủ thông tin
     * Method tiện lợi để test happy path
     */
    public void register(String name, String email, String password, String confirmPassword) {
        fillName(name);
        fillEmail(email);
        fillPassword(password);
        fillConfirmPassword(confirmPassword);
        checkTermsCheckbox();
        clickRegister();
    }

    /**
     * Đăng ký nhanh với password giống nhau
     */
    public void registerQuick(String name, String email, String password) {
        register(name, email, password, password);
    }

    // ========================================
    // VERIFICATION METHODS - Kiểm tra trạng thái
    // ========================================

    /**
     * Kiểm tra có hiển thị error message không
     */
    public boolean hasErrorMessage() {
        return page.locator(ERROR_MESSAGE_BOX).count() > 0;
    }

    /**
     * Lấy nội dung error message từ server
     */
    public String getErrorMessage() {
        return page.textContent(ERROR_MESSAGE_BOX);
    }

    /**
     * Kiểm tra nút Register có enabled không
     */
    public boolean isRegisterButtonEnabled() {
        return page.locator(REGISTER_BUTTON).isEnabled();
    }

    /**
     * Lấy validation message của field cụ thể
     */
    public String getValidationMessage(String selector) {
        return page.locator(selector)
                .evaluate("el => el.validationMessage")
                .toString();
    }

    /**
     * Lấy title của page
     */
    public String getTitle() {
        return page.title();
    }
}
