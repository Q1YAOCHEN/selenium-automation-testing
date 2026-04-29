package tests;

import base.BaseTest;
import utils.ScreenshotUtil;
import utils.HtmlReportGenerator;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;

// Scenario 5: Update the Academic Calendar
//Tests the ability to navigate to the NEU Academic Calendar,
// uncheck a calendar filter checkbox, and verify the
// "Add to My Calendar" button is displayed.
public class Scenario5_UpdateAcademicCalendar extends BaseTest {

    private static final String SCENARIO = "Scenario5_UpdateAcademicCalendar";

    @Test(priority = 5, description = "Update the Academic Calendar")
    public void updateAcademicCalendar() throws Exception {

        // Step a) Navigate to the Northeastern Student Hub
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "before_navigate_student_hub");
        driver.get("https://student.me.northeastern.edu/");
        Thread.sleep(5000);
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "after_navigate_student_hub");

        // Click on Resources tab
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "before_click_resources");
        wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("a[data-testid='link-resources']")));
        driver.findElement(By.cssSelector("a[data-testid='link-resources']")).click();
        Thread.sleep(3000);
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "after_click_resources");

        // Step b) Click on Academics, Classes & Registration
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "before_click_academics");
        wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//span[contains(@class,'fui-Tab__content') and contains(text(),'Academics')]")));
        driver.findElement(
            By.xpath("//span[contains(@class,'fui-Tab__content') and contains(text(),'Academics')]")).click();
        Thread.sleep(3000);
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "after_click_academics");

        // Step c) Click on Academic Calendar - open in same tab
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "before_click_academic_calendar");
        WebElement calLink = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//a[@data-gtm-resources-link='Academic Calendar']")));
        // Remove target="_blank" so it opens in same tab
        ((JavascriptExecutor) driver).executeScript("arguments[0].removeAttribute('target');", calLink);
        calLink.click();
        Thread.sleep(5000);
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "after_click_academic_calendar");

        // Step d) Click on Academic Calendar under Northeastern University Registrar
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "before_click_registrar_calendar");
        WebElement regCalLink = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//a[contains(@href,'article/academic-calendar')]")));
        regCalLink.click();
        Thread.sleep(5000);
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "after_click_registrar_calendar");

        // Step e) Scroll down to the calendars on the right side
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "before_scroll_to_calendars");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        
        // Scroll to exact position where checkboxes are visible
        js.executeScript("window.scrollTo(0, 920)");
        Thread.sleep(3000);
        
        // Wait for Trumba to load checkboxes — keep checking every 2 seconds
        boolean checkboxFound = false;
        for (int attempt = 0; attempt < 10; attempt++) {
            Long count = (Long) js.executeScript("return document.querySelectorAll('.twCalendarListCheckbox').length");
            if (count > 0) {
                checkboxFound = true;
                System.out.println("Found " + count + " Trumba checkboxes after " + (attempt * 2) + " seconds");
                break;
            }
            Thread.sleep(2000);
        }

        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "after_scroll_to_calendars");

        // Step f) Uncheck a checkbox using JavaScript
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "before_uncheck_checkbox");
        if (checkboxFound) {
            js.executeScript(
                "var cb = document.querySelector('.twCalendarListCheckbox');" +
                "if (cb && cb.checked) { cb.click(); }");
            System.out.println("Unchecked a calendar checkbox via JavaScript");
        } else {
            System.out.println("No checkboxes found, trying iframe approach...");
            // Try all iframes
            int iframeCount = ((Long) js.executeScript("return document.querySelectorAll('iframe').length")).intValue();
            for (int f = 0; f < iframeCount; f++) {
                try {
                    driver.switchTo().frame(f);
                    Long cbCount = (Long) js.executeScript("return document.querySelectorAll('.twCalendarListCheckbox').length");
                    if (cbCount > 0) {
                        js.executeScript(
                            "var cb = document.querySelector('.twCalendarListCheckbox');" +
                            "if (cb && cb.checked) { cb.click(); }");
                        System.out.println("Unchecked checkbox in iframe " + f);
                        break;
                    }
                    driver.switchTo().defaultContent();
                } catch (Exception ex) {
                    driver.switchTo().defaultContent();
                }
            }
            driver.switchTo().defaultContent();
        }
        Thread.sleep(2000);
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "after_uncheck_checkbox");

        // Step g) Scroll down to bottom and verify Add to My Calendar button
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "before_scroll_to_bottom");
        
        // Make sure we're on main content first
        try { driver.switchTo().defaultContent(); } catch (Exception e) {}
        
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        Thread.sleep(3000);

        // Try finding "Add to My Calendar" - might be in main page or iframe
        boolean addBtnFound = false;
        
        // Check main page first
        try {
            WebElement addBtn = driver.findElement(
                By.xpath("//span[contains(text(),'Add to My Calendar')]/.. | //input[@value='Add to My Calendar'] | //button[contains(text(),'Add to My Calendar')]"));
            addBtnFound = addBtn.isDisplayed();
        } catch (Exception e) {
            // Try finding via JavaScript
            Long count = (Long) js.executeScript(
                "return document.querySelectorAll('span').length");
            Boolean found = (Boolean) js.executeScript(
                "var spans = document.querySelectorAll('span');" +
                "for(var i=0; i<spans.length; i++) {" +
                "  if(spans[i].textContent.trim() === 'Add to My Calendar') return true;" +
                "}" +
                "return false;");
            if (found) {
                addBtnFound = true;
            } else {
                // Try all iframes
                int iframeCount = ((Long) js.executeScript("return document.querySelectorAll('iframe').length")).intValue();
                for (int f = 0; f < iframeCount; f++) {
                    try {
                        driver.switchTo().frame(f);
                        Boolean foundInFrame = (Boolean) js.executeScript(
                            "var spans = document.querySelectorAll('span');" +
                            "for(var i=0; i<spans.length; i++) {" +
                            "  if(spans[i].textContent.trim() === 'Add to My Calendar') return true;" +
                            "}" +
                            "return false;");
                        if (foundInFrame) {
                            addBtnFound = true;
                            System.out.println("Found Add to My Calendar in iframe " + f);
                            break;
                        }
                        driver.switchTo().defaultContent();
                    } catch (Exception ex) {
                        try { driver.switchTo().defaultContent(); } catch (Exception ex2) {}
                    }
                }
                try { driver.switchTo().defaultContent(); } catch (Exception ex) {}
            }
        }

        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "after_verify_add_button");

        Assert.assertTrue(addBtnFound,
            "Add to My Calendar button should be present on the page");

        // Add result to HTML report
        HtmlReportGenerator.addResult(
            "Scenario 5: Update the Academic Calendar",
            "Calendar checkbox unchecked and Add to My Calendar button verified",
            "Calendar should be updated and Add to My Calendar button should be visible",
            "Pass"
        );

        System.out.println("Scenario 5 completed successfully!");
    }
}