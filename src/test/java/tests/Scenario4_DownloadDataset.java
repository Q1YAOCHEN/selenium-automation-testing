package tests;

import base.BaseTest;
import utils.ScreenshotUtil;
import utils.HtmlReportGenerator;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;
import java.io.File;

// Scenario 4: Download a Dataset (NEGATIVE SCENARIO - This test must FAIL)
public class Scenario4_DownloadDataset extends BaseTest {

    private static final String SCENARIO = "Scenario4_DownloadDataset";

    @Test(priority = 4, description = "Download a dataset - Negative scenario (must fail)")
    public void downloadDataset() throws Exception {

        try {
            // Step a) Open Scholar OneSearch
            ScreenshotUtil.takeScreenshot(driver, SCENARIO, "before_navigate_onesearch");
            driver.get("https://onesearch.library.northeastern.edu/discovery/search?vid=01NEU_INST:NU&lang=en");
            Thread.sleep(3000);
            ScreenshotUtil.takeScreenshot(driver, SCENARIO, "after_navigate_onesearch");

            // Click on Digital Repository Service - open in same tab
            ScreenshotUtil.takeScreenshot(driver, SCENARIO, "before_click_drs");
            WebElement drsLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'digital repository service')]/..")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].removeAttribute('target');", drsLink);
            drsLink.click();
            Thread.sleep(3000);
            ScreenshotUtil.takeScreenshot(driver, SCENARIO, "after_click_drs");

            // Step b) Scroll down and click on Datasets under Featured Content
            ScreenshotUtil.takeScreenshot(driver, SCENARIO, "before_click_datasets");
            JavascriptExecutor js = (JavascriptExecutor) driver;

            WebElement datasetsLink = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//a[contains(@href,'/datasets')]")));
            js.executeScript("arguments[0].scrollIntoView({block: 'center'});", datasetsLink);
            Thread.sleep(1000);
            datasetsLink.click();
            Thread.sleep(3000);
            ScreenshotUtil.takeScreenshot(driver, SCENARIO, "after_click_datasets");

            // Click on the first available dataset
            ScreenshotUtil.takeScreenshot(driver, SCENARIO, "before_open_dataset");
            WebElement firstDataset = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(@href,'/files/neu:')]")));
            firstDataset.click();
            Thread.sleep(3000);
            ScreenshotUtil.takeScreenshot(driver, SCENARIO, "after_open_dataset");

            // Step c) Click on "Zip File" to start download
            ScreenshotUtil.takeScreenshot(driver, SCENARIO, "before_click_zip_download");
            WebElement zipLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[@title='Zip File' or contains(text(),'Zip File')]")));
            js.executeScript("arguments[0].scrollIntoView({block: 'center'});", zipLink);
            Thread.sleep(1000);
            // Remove target="_blank" so download stays in same tab
            js.executeScript("arguments[0].removeAttribute('target');", zipLink);
            zipLink.click();
            ScreenshotUtil.takeScreenshot(driver, SCENARIO, "after_click_zip_download");

            // Wait only 5 seconds - NOT enough for a large dataset file
            System.out.println("Waiting 5 seconds for download (not enough for large file)...");
            Thread.sleep(5000);

            // INTENTIONAL FAILURE: Assert that a specific expected file exists
            // We expect a file called "expected_research_data.zip" which will never exist
            String userDownloads = System.getProperty("user.home") + "/Downloads";
            File downloadsDir = new File(userDownloads);
            boolean expectedFileFound = false;

            if (downloadsDir.exists() && downloadsDir.isDirectory()) {
                File[] files = downloadsDir.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.getName().equals("expected_research_data.zip")) {
                            expectedFileFound = true;
                            break;
                        }
                    }
                }
            }

            // This assertion WILL FAIL — the downloaded file name won't match
            Assert.assertTrue(expectedFileFound,
                "NEGATIVE SCENARIO: Expected file 'expected_research_data.zip' not found. " +
                "The dataset was downloaded but with a different file name than expected.");

        } catch (AssertionError e) {
            ScreenshotUtil.takeScreenshot(driver, SCENARIO, "scenario_failed_as_expected");

            HtmlReportGenerator.addResult(
                "Scenario 4: Download a Dataset (Negative)",
                "Dataset download did not complete within time limit",
                "Dataset should be fully downloaded",
                "Fail"
            );

            System.out.println("Scenario 4 FAILED as expected (Negative Scenario)!");
            throw e;

        } catch (Exception e) {
            ScreenshotUtil.takeScreenshot(driver, SCENARIO, "scenario_failed_with_exception");

            HtmlReportGenerator.addResult(
                "Scenario 4: Download a Dataset (Negative)",
                "Download process failed - " + e.getMessage(),
                "Dataset should be fully downloaded",
                "Fail"
            );

            System.out.println("Scenario 4 FAILED as expected (Negative Scenario)!");
            Assert.fail("NEGATIVE SCENARIO: Download process failed - " + e.getMessage());
        }
    }
}