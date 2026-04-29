package utils;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

// Utility class to capture screenshots before and after each step
public class ScreenshotUtil {

    private static final String SCREENSHOTS_DIR = "screenshots";

    // Takes a screenshot and saves it in a folder named after the scenario
    // stepName should describe the action, e.g., "before_login", "after_login"
    public static void takeScreenshot(WebDriver driver, String scenarioName, String stepName) {
        try {
            // Create scenario folder if it doesn't exist
            String scenarioDir = SCREENSHOTS_DIR + File.separator + scenarioName;
            File directory = new File(scenarioDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Generate timestamp for unique file names
            String timestamp = new SimpleDateFormat("HHmmss").format(new Date());

            // Capture screenshot
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);

            // Save screenshot with descriptive name
            String fileName = stepName + "_" + timestamp + ".png";
            File destination = new File(scenarioDir + File.separator + fileName);
            FileUtils.copyFile(source, destination);

            System.out.println("Screenshot saved: " + destination.getPath());
        } catch (IOException e) {
            System.out.println("Failed to capture screenshot: " + e.getMessage());
        }
    }
}