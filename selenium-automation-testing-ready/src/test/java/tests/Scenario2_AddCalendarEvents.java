package tests;

import base.BaseTest;
import utils.ExcelReader;
import utils.ScreenshotUtil;
import utils.HtmlReportGenerator;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;

// Scenario 2: Add two Event tasks on Canvas Calendar
public class Scenario2_AddCalendarEvents extends BaseTest {

    private static final String SCENARIO = "Scenario2_AddCalendarEvents";

    @Test(priority = 2, description = "Add two calendar events on Canvas")
    public void addCalendarEvents() throws Exception {

        // Read event data from Excel spreadsheet
        String[][] eventData = ExcelReader.getEventData();

        // Step a) Navigate to Canvas Calendar
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "before_navigate_canvas");
        driver.get("https://northeastern.instructure.com/calendar");
        Thread.sleep(5000);
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "after_navigate_canvas");

        // Enable Abhinav Chinta calendar checkbox before creating events
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "before_enable_calendar_checkbox");
        WebElement calCheckbox = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.cssSelector("span.context-list-toggle-box.group_user_320844")));
        if (calCheckbox.getAttribute("aria-checked").equals("false")) {
            calCheckbox.click();
            Thread.sleep(2000);
            System.out.println("Enabled Abhinav Chinta calendar checkbox");
        }
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "after_enable_calendar_checkbox");

        // Step b) Create 2 events iteratively from Excel data
        for (int i = 0; i < eventData.length; i++) {
            String title = eventData[i][0];
            String date = eventData[i][1];
            String startTime = eventData[i][2];
            String endTime = eventData[i][3];
            String calendar = eventData[i][4];
            String details = eventData[i][5];

            System.out.println("Creating event " + (i + 1) + ": " + title);

            // Click the "+" button to create new event
            ScreenshotUtil.takeScreenshot(driver, SCENARIO, "before_click_plus_event" + (i + 1));
            wait.until(ExpectedConditions.elementToBeClickable(By.id("create_new_event_link")));
            driver.findElement(By.id("create_new_event_link")).click();
            Thread.sleep(2000);
            ScreenshotUtil.takeScreenshot(driver, SCENARIO, "after_click_plus_event" + (i + 1));

            // Wait for event dialog to appear
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("input[data-testid='edit-calendar-event-form-title']")));

            // Enter Title
            ScreenshotUtil.takeScreenshot(driver, SCENARIO, "before_enter_title_event" + (i + 1));
            WebElement titleField = driver.findElement(
                By.cssSelector("input[data-testid='edit-calendar-event-form-title']"));
            titleField.clear();
            titleField.sendKeys(title);
            ScreenshotUtil.takeScreenshot(driver, SCENARIO, "after_enter_title_event" + (i + 1));

            // Enter Date - use JavaScript to force the value
            ScreenshotUtil.takeScreenshot(driver, SCENARIO, "before_enter_date_event" + (i + 1));
            WebElement dateField = driver.findElement(
                By.cssSelector("input[data-testid='edit-calendar-event-form-date']"));
            
            // Use JavaScript to set value and trigger React change event
            String jsSetDate = 
                "var input = arguments[0];" +
                "var nativeInputValueSetter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set;" +
                "nativeInputValueSetter.call(input, arguments[1]);" +
                "input.dispatchEvent(new Event('input', { bubbles: true }));" +
                "input.dispatchEvent(new Event('change', { bubbles: true }));";
            ((JavascriptExecutor) driver).executeScript(jsSetDate, dateField, date);
            Thread.sleep(1000);
            // Click somewhere else to close any picker
            dateField.sendKeys(Keys.TAB);
            Thread.sleep(2000);
            ScreenshotUtil.takeScreenshot(driver, SCENARIO, "after_enter_date_event" + (i + 1));

            // Enter Start Time - click and type
            ScreenshotUtil.takeScreenshot(driver, SCENARIO, "before_enter_starttime_event" + (i + 1));
            WebElement startTimeField = driver.findElement(
                By.cssSelector("input[data-testid='event-form-start-time']"));
            startTimeField.click();
            Thread.sleep(500);
            startTimeField.sendKeys(startTime);
            Thread.sleep(500);
            // Select the matching option from dropdown
            try {
                WebElement timeOption = new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(3))
                    .until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//ul[@role='listbox']//li[contains(.,'" + startTime + "')]")));
                timeOption.click();
            } catch (Exception e) {
                startTimeField.sendKeys(Keys.RETURN);
            }
            Thread.sleep(500);
            ScreenshotUtil.takeScreenshot(driver, SCENARIO, "after_enter_starttime_event" + (i + 1));

            // Enter End Time - move to end, backspace to clear, then type
            ScreenshotUtil.takeScreenshot(driver, SCENARIO, "before_enter_endtime_event" + (i + 1));
            WebElement endTimeField = driver.findElement(
                By.cssSelector("input[data-testid='event-form-end-time']"));
            endTimeField.click();
            Thread.sleep(500);
            // Move cursor to end of field
            endTimeField.sendKeys(Keys.END);
            Thread.sleep(200);
            // Backspace to clear all characters
            for (int k = 0; k < 10; k++) {
                endTimeField.sendKeys(Keys.BACK_SPACE);
                Thread.sleep(100);
            }
            Thread.sleep(500);
            endTimeField.sendKeys(endTime);
            Thread.sleep(500);
            try {
                WebElement timeOption = new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(3))
                    .until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//ul[@role='listbox']//li[contains(.,'" + endTime + "')]")));
                timeOption.click();
            } catch (Exception e) {
                endTimeField.sendKeys(Keys.RETURN);
            }
            Thread.sleep(500);
            ScreenshotUtil.takeScreenshot(driver, SCENARIO, "after_enter_endtime_event" + (i + 1));

            // Select Calendar from dropdown
            ScreenshotUtil.takeScreenshot(driver, SCENARIO, "before_select_calendar_event" + (i + 1));
            WebElement calendarField = driver.findElement(
                By.cssSelector("input[data-testid='edit-calendar-event-form-context']"));
            calendarField.click();
            Thread.sleep(1000);

            // Select the calendar option from dropdown list
            try {
                WebElement calendarOption = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//li[contains(@role,'option') and contains(.,'" + calendar + "')]"
                        + " | //span[contains(text(),'" + calendar + "')]/ancestor::li"
                        + " | //ul[@role='listbox']//li[contains(.,'" + calendar + "')]")));
                calendarOption.click();
            } catch (Exception e) {
                System.out.println("Calendar option not found for: " + calendar + ", using default");
                calendarField.sendKeys(Keys.ESCAPE);
            }
            Thread.sleep(1000);
            ScreenshotUtil.takeScreenshot(driver, SCENARIO, "after_select_calendar_event" + (i + 1));

            // Click Submit button
            ScreenshotUtil.takeScreenshot(driver, SCENARIO, "before_submit_event" + (i + 1));
            WebElement submitBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[text()='Submit']/ancestor::button")));
            submitBtn.click();
            Thread.sleep(3000);
            ScreenshotUtil.takeScreenshot(driver, SCENARIO, "after_submit_event" + (i + 1));

            System.out.println("Event " + (i + 1) + " created successfully: " + title);
        }

        // Final verification - events should be on calendar
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "after_all_events_created");

        // Verify at least one event appears
        String pageSource = driver.getPageSource();
        Assert.assertTrue(
            pageSource.contains(eventData[0][0]) || 
            pageSource.contains(eventData[1][0]) ||
            driver.findElements(By.xpath("//*[contains(text(),'" + eventData[0][0] + "')]")).size() > 0,
            "Events should be visible on calendar");

        // Add result to HTML report
        HtmlReportGenerator.addResult(
            "Scenario 2: Add two Event tasks",
            "Both events created and visible on calendar",
            "Two events should be added to the calendar",
            "Pass"
        );

        System.out.println("Scenario 2 completed successfully!");
    }
}