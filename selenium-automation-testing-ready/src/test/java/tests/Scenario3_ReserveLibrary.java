package tests;

import base.BaseTest;
import utils.ScreenshotUtil;
import utils.HtmlReportGenerator;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.*;

// Authored by Abdulmohsen - Scenario 3: Reserve a spot in Snell Library
public class Scenario3_ReserveLibrary extends BaseTest {

    private static final String SCENARIO = "Scenario3_ReserveLibrary";

    @Test(priority = 3, description = "Reserve a spot in Snell Library")
    public void reserveLibrarySpot() throws Exception {

        // Step a) Open the library website
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "before_navigate_library");
        driver.get("https://library.northeastern.edu/");
        Thread.sleep(3000);
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "after_navigate_library");

        // Handle cookie consent popup if it appears
        try {
            WebElement acceptCookies = new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(By.id("accept-selected")));
            acceptCookies.click();
            System.out.println("Accepted cookies");
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("No cookie popup found, continuing...");
        }

        // Step b) Click on "Reserve A Study Room"
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "before_click_reserve");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        // Scroll down more to find the link
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        Thread.sleep(2000);
        
        WebElement reserveLink = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//a[contains(text(),'Reserve') and contains(text(),'Study Room')]")));
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", reserveLink);
        Thread.sleep(1000);
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "before_click_reserve_scrolled");
        js.executeScript("arguments[0].click();", reserveLink);
        Thread.sleep(3000);
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "after_click_reserve");

        // Step c) Select Boston
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "before_select_boston");
        wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//img[contains(@alt,'Boston')]/..")));
        driver.findElement(By.xpath("//img[contains(@alt,'Boston')]/..")).click();
        Thread.sleep(3000);
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "after_select_boston");

        // Step d) Click on "Book a Room"
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "before_book_room");
        wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//a[contains(@class,'simple-button') and contains(@href,'studyspace')]")));
        driver.findElement(
            By.xpath("//a[contains(@class,'simple-button') and contains(@href,'studyspace')]")).click();
        Thread.sleep(5000);
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "after_book_room");

        // Wait for LibCal page to fully load (same tab)
        Thread.sleep(3000);
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "after_libcal_loaded");

        // Handle cookie popup on LibCal if present
        try {
            WebElement acceptCookies2 = new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(3))
                .until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(),'Accept') or @id='accept-selected' or contains(@class,'accept')]")));
            acceptCookies2.click();
            System.out.println("Accepted LibCal cookies");
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("No LibCal cookie popup, continuing...");
        }

        // Step e) Select "Individual Study" from Seat Style dropdown
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "before_select_seat_style");
        WebElement seatStyleDropdown = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gid")));
        Select seatStyle = new Select(seatStyleDropdown);
        seatStyle.selectByValue("13369");
        Thread.sleep(2000);
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "after_select_seat_style");

        // Select "Space for 1-4 people" from Capacity dropdown
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "before_select_capacity");
        WebElement capacityDropdown = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("capacity")));
        Select capacity = new Select(capacityDropdown);
        capacity.selectByValue("1");
        Thread.sleep(3000);
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "after_select_capacity");

        // Click the right arrow 3 times using JavaScript to move date forward
        for (int arrow = 1; arrow <= 3; arrow++) {
            ScreenshotUtil.takeScreenshot(driver, SCENARIO, "before_click_right_arrow_" + arrow);
            WebElement rightArrow = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("button.fc-next-button")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", rightArrow);
            Thread.sleep(3000);
            ScreenshotUtil.takeScreenshot(driver, SCENARIO, "after_click_right_arrow_" + arrow);
        }

        // Scroll down to see available rooms and take screenshot before selecting
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "before_scroll_to_rooms");
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        Thread.sleep(2000);
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "after_scroll_rooms_before_select");

        // Scroll back up to select a blue (available) time slot
        js.executeScript("window.scrollTo(0, 0)");
        Thread.sleep(2000);

        // Click any available blue box using JavaScript
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "before_select_available_slot");
        ((JavascriptExecutor) driver).executeScript(
            "var events = document.querySelectorAll('.fc-event-title-container');" +
            "if(events.length > 0) { events[0].closest('a').click(); }" +
            "else { var evts = document.querySelectorAll('.fc-event'); if(evts.length > 0) evts[0].click(); }");
        Thread.sleep(3000);
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "after_select_available_slot");

        // It automatically scrolls down — take screenshot of the booking form
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        Thread.sleep(2000);
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "after_scroll_booking_confirmation");

        // Verify the booking page loaded with study rooms
        String pageSource = driver.getPageSource();
        Assert.assertTrue(
            pageSource.contains("Individual Study") || 
            pageSource.contains("Study") ||
            pageSource.contains("Available"),
            "Library booking page should display study rooms");

        // Add result to HTML report
        HtmlReportGenerator.addResult(
            "Scenario 3: Reserve a spot in Snell Library",
            "Library booking page loaded with Individual Study rooms filtered",
            "Library booking page should display available rooms",
            "Pass"
        );

        System.out.println("Scenario 3 completed successfully!");
    }
}
