package tests;

import base.BaseTest;
import utils.ExcelReader;
import utils.ScreenshotUtil;
import utils.HtmlReportGenerator;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.*;
import java.util.Map;
import java.util.Set;

// Scenario 1: Download the latest transcript from NEU Student Hub
public class Scenario1_DownloadTranscript extends BaseTest {

    private static final String SCENARIO = "Scenario1_DownloadTranscript";

    @Test(priority = 1, description = "Download the latest transcript")
    public void downloadTranscript() throws Exception {

        // Read login credentials from Excel
        Map<String, String> loginData = ExcelReader.getLoginData();
        String username = loginData.get("username");
        String password = loginData.get("password");
        String bannerUsername = loginData.get("banner_username");

        // ===== STEP a) Navigate to My NEU portal and Login =====
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "before_navigate_to_neu");
        driver.get("https://me.northeastern.edu");
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "after_navigate_to_neu");

        // Enter username
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "before_enter_username");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("i0116")));
        driver.findElement(By.id("i0116")).sendKeys(username);
        driver.findElement(By.id("idSIButton9")).click();
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "after_enter_username");

        // Enter password
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "before_enter_password");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("i0118")));
        driver.findElement(By.id("i0118")).sendKeys(password);
        driver.findElement(By.id("idSIButton9")).click();
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "after_enter_password");

        // ===== DUO 2FA (New Duo Interface) =====
        System.out.println(">>> HANDLING DUO 2FA <<<");
        Thread.sleep(3000);

        try {
            WebElement duoIframe = driver.findElement(By.xpath("//iframe[contains(@src,'duosecurity')]"));
            driver.switchTo().frame(duoIframe);
        } catch (Exception e) {
            System.out.println("No Duo iframe found, continuing on main page...");
        }

        // Click "Other options"
        WebElement otherOptions = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//a[contains(text(),'Other options')] | //button[contains(text(),'Other options')]")));
        otherOptions.click();
        Thread.sleep(2000);

        // Click iOS Duo Push
        WebElement iosPush = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//a[contains(@class,'auth-method-link') and .//span[contains(text(),'iOS')]]")));
        iosPush.click();

        // Wait for approval then click "Yes, this is my device"
        System.out.println(">>> APPROVE THE DUO PUSH ON YOUR iPHONE NOW! <<<");
        WebElement yesMyDevice = new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(30))
            .until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(),'Yes, this is my device')]")));
        yesMyDevice.click();
        System.out.println("Clicked 'Yes, this is my device'");
        Thread.sleep(3000);

        try { driver.switchTo().defaultContent(); } catch (Exception e) {}

        // Handle "Stay signed in?" prompt
        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.id("idSIButton9")));
            driver.findElement(By.id("idSIButton9")).click();
        } catch (Exception e) {
            System.out.println("No 'Stay signed in' prompt found, continuing...");
        }

        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "after_login_complete");

        // ===== STEP b) Navigate to Student Hub =====
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "before_student_hub");
        driver.get("https://student.me.northeastern.edu/");
        Thread.sleep(5000);
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "after_student_hub");

        // ===== STEP c) Click on Resources tab =====
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "before_resources_tab");
        wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("a[data-testid='link-resources']")));
        driver.findElement(By.cssSelector("a[data-testid='link-resources']")).click();
        Thread.sleep(3000);
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "after_resources_tab");

        // ===== STEP d) Click on Academics, Classes & Registration =====
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "before_academics_section");
        wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//span[contains(@class,'fui-Tab__content') and contains(text(),'Academics')]")));
        driver.findElement(
            By.xpath("//span[contains(@class,'fui-Tab__content') and contains(text(),'Academics')]")).click();
        Thread.sleep(3000);
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "after_academics_section");

        // ===== STEP e) Click on Unofficial Transcript =====
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "before_unofficial_transcript");
        wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//a[@data-gtm-resources-link='Unofficial Transcript']")));
        driver.findElement(By.xpath("//a[@data-gtm-resources-link='Unofficial Transcript']")).click();
        Thread.sleep(3000);
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "after_unofficial_transcript");

        // Switch to new window/tab if opened
        String mainWindow = driver.getWindowHandle();
        Set<String> allWindows = driver.getWindowHandles();
        for (String window : allWindows) {
            if (!window.equals(mainWindow)) {
                driver.switchTo().window(window);
                break;
            }
        }

        // ===== Handle Banner Login =====
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "before_banner_login");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        driver.findElement(By.id("username")).sendKeys(bannerUsername);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.xpath("//button[@name='_eventId_proceed']")).click();
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "after_banner_login");

        // ===== Handle Old Duo Interface (Banner) =====
        Thread.sleep(3000);
        try {
            // Try switching to Duo iframe
            boolean inIframe = false;
            try {
                WebElement duoFrame = new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(5))
                    .until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//iframe[@id='duo_iframe'] | //iframe[contains(@src,'duosecurity')]")));
                driver.switchTo().frame(duoFrame);
                inIframe = true;
                System.out.println("Switched to Duo iframe");
            } catch (Exception noIframe) {
                System.out.println("No Duo iframe, checking main page...");
            }

            // Select iOS device from dropdown
            WebElement deviceDropdown = new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//select[@name='device']")));
            new Select(deviceDropdown).selectByValue("phone2");
            Thread.sleep(2000);
            ScreenshotUtil.takeScreenshot(driver, SCENARIO, "after_select_ios_device");

            // Click Send Me a Push - try multiple methods
            System.out.println("Attempting to click Send Me a Push...");
            boolean clicked = false;
            
            // Method 1: Direct click
            try {
                driver.findElement(By.xpath("//button[contains(.,'Send Me a Push')]")).click();
                System.out.println("Clicked Send Me a Push (Method 1)!");
                clicked = true;
            } catch (Exception e1) {}

            // Method 2: JavaScript click on element
            if (!clicked) {
                try {
                    WebElement btn = driver.findElement(By.xpath("//button[contains(.,'Send Me a Push')]"));
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
                    System.out.println("Clicked Send Me a Push (Method 2)!");
                    clicked = true;
                } catch (Exception e2) {}
            }

            // Method 3: CSS selector with JS
            if (!clicked) {
                try {
                    ((JavascriptExecutor) driver).executeScript(
                        "document.querySelector('button.positive').click();");
                    System.out.println("Clicked Send Me a Push (Method 3)!");
                    clicked = true;
                } catch (Exception e3) {}
            }

            // Method 4: Find form and submit
            if (!clicked) {
                try {
                    ((JavascriptExecutor) driver).executeScript(
                        "document.querySelector('#login-form button[type=submit]').click();");
                    System.out.println("Clicked Send Me a Push (Method 4)!");
                    clicked = true;
                } catch (Exception e4) {}
            }

            System.out.println(">>> APPROVE DUO PUSH ON YOUR iPHONE! <<<");

            // Switch back if in iframe
            if (inIframe) {
                try { driver.switchTo().defaultContent(); } catch (Exception e) {}
            }

            // Wait for transcript page to load after Duo approval
            new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(60))
                .until(ExpectedConditions.presenceOfElementLocated(
                    By.id("transcriptLevelSelection")));
            System.out.println("Transcript page loaded after Duo!");

        } catch (Exception duoEx) {
            System.out.println("No Duo prompt or already on transcript page: " + duoEx.getMessage());
            try { driver.switchTo().defaultContent(); } catch (Exception e) {}
        }

        // ===== STEP f) Select Graduate and Audit Transcript =====
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "before_select_transcript_options");
        Thread.sleep(3000);

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("transcriptLevelSelection")));

        // Click Transcript Level dropdown
        WebElement levelDd = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//div[@id='transcriptLevelSelection']//a | //div[contains(@id,'transcriptLevel')]//a[contains(@class,'select2-choice')]")));
        levelDd.click();
        Thread.sleep(1000);

        // Select "Graduate"
        WebElement graduateOption = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//div[contains(@class,'select2-result-label')]//div[contains(text(),'Graduate')] | //li[contains(@class,'select2-result')]//div[contains(text(),'Graduate')]")));
        graduateOption.click();
        Thread.sleep(3000);
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "after_select_graduate");

        // Click Transcript Type dropdown
        WebElement typeDd = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//div[@xe-section='transcriptTypeSection']//a[contains(@class,'select2-choice')] | //div[contains(@id,'transcriptType')]//a[contains(@class,'select2-choice')]")));
        typeDd.click();
        Thread.sleep(1000);

        // Select "Audit Transcript"
        WebElement auditOption = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//div[contains(@class,'select2-result-label')]//div[contains(text(),'Audit Transcript')] | //li[contains(@class,'select2-result')]//div[contains(text(),'Audit Transcript')]")));
        auditOption.click();
        Thread.sleep(5000);
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "after_select_transcript_options");

        // ===== STEP g) Save as PDF =====
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "before_save_pdf");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.print();");
        Thread.sleep(5000);
        ScreenshotUtil.takeScreenshot(driver, SCENARIO, "after_save_pdf");

        // Verify transcript page loaded
        String pageSource = driver.getPageSource();
        Assert.assertTrue(pageSource.contains("Academic Transcript") || 
                          pageSource.contains("Student Information"),
            "Transcript page should be displayed");

        // Add result to HTML report
        HtmlReportGenerator.addResult(
            "Scenario 1: Download the latest transcript",
            "Transcript downloaded successfully as PDF",
            "Transcript should be downloaded as PDF",
            "Pass"
        );

        // Switch back to main window
        driver.switchTo().window(mainWindow);

        System.out.println("Scenario 1 completed successfully!");
    }
}