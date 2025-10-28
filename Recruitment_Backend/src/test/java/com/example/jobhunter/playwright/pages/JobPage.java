package com.example.jobhunter.playwright.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Locator;

import java.util.List;

/**
 * Page Object Model cho Job Pages
 */
public class JobPage {
    private final Page page;
    
    // Selectors
    private static final String SEARCH_INPUT = "input[data-testid='job-search']";
    private static final String SEARCH_BUTTON = "button[data-testid='search-button']";
    private static final String JOB_CARD = ".job-card";
    private static final String JOB_TITLE = ".job-title";
    private static final String JOB_COMPANY = ".job-company";
    private static final String JOB_SALARY = ".job-salary";
    private static final String JOB_LOCATION = ".job-location";
    private static final String FAVORITE_BUTTON = "button[data-testid='favorite-button']";
    private static final String APPLY_BUTTON = "button[data-testid='apply-button']";
    private static final String FILTER_LOCATION = "select[name='location']";
    private static final String FILTER_SALARY = "select[name='salary']";
    private static final String PAGINATION_NEXT = "button[data-testid='pagination-next']";
    private static final String PAGINATION_PREV = "button[data-testid='pagination-prev']";

    public JobPage(Page page) {
        this.page = page;
    }

    public void navigateToAllJobs() {
        page.navigate(System.getProperty("frontend.url", "http://localhost:5173") + "/jobs");
        page.waitForLoadState();
    }

    public void navigateToSavedJobs() {
        page.navigate(System.getProperty("frontend.url", "http://localhost:5173") + "/jobs/saved");
        page.waitForLoadState();
    }

    public void searchJobs(String keyword) {
        page.fill(SEARCH_INPUT, keyword);
        page.click(SEARCH_BUTTON);
        page.waitForLoadState();
    }

    public int getJobCount() {
        return page.locator(JOB_CARD).count();
    }

    public void clickFirstJob() {
        page.locator(JOB_CARD).first().click();
    }

    public void favoriteFirstJob() {
        page.locator(JOB_CARD).first().locator(FAVORITE_BUTTON).click();
    }

    public void applyToFirstJob() {
        page.locator(JOB_CARD).first().locator(APPLY_BUTTON).click();
    }

    public String getFirstJobTitle() {
        return page.locator(JOB_CARD).first().locator(JOB_TITLE).textContent();
    }

    public String getFirstJobCompany() {
        return page.locator(JOB_CARD).first().locator(JOB_COMPANY).textContent();
    }

    public String getFirstJobSalary() {
        return page.locator(JOB_CARD).first().locator(JOB_SALARY).textContent();
    }

    public String getFirstJobLocation() {
        return page.locator(JOB_CARD).first().locator(JOB_LOCATION).textContent();
    }

    public void filterByLocation(String location) {
        page.selectOption(FILTER_LOCATION, location);
        page.waitForLoadState();
    }

    public void filterBySalary(String salary) {
        page.selectOption(FILTER_SALARY, salary);
        page.waitForLoadState();
    }

    public void goToNextPage() {
        page.click(PAGINATION_NEXT);
        page.waitForLoadState();
    }

    public void goToPreviousPage() {
        page.click(PAGINATION_PREV);
        page.waitForLoadState();
    }

    public boolean hasJobs() {
        return getJobCount() > 0;
    }

    public boolean isFirstJobFavorited() {
        Locator favoriteButton = page.locator(JOB_CARD).first().locator(FAVORITE_BUTTON);
        return favoriteButton.getAttribute("class").contains("favorited");
    }

    public List<String> getAllJobTitles() {
        return page.locator(JOB_TITLE).allTextContents();
    }

    public void waitForJobsToLoad() {
        page.waitForSelector(JOB_CARD);
    }
}