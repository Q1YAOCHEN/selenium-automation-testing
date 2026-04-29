package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

// Base test class that all test classes will extend
// Uses a SINGLE browser session shared across ALL scenarios
// Fresh session every time (no saved profile) for presentation
public class BaseTest {

    protected static WebDriver driver;
    protected static WebDriverWait wait;

    @BeforeSuite
    public void setUpSuite() {
        // Setup ChromeDriver using WebDriverManager
        WebDriverManager.chromedriver().setup();

        // Configure Chrome options
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");

        // Set download directory for file downloads
        String downloadPath = System.getProperty("user.dir") + "/downloads";
        new java.io.File(downloadPath).mkdirs();
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", downloadPath);
        prefs.put("download.prompt_for_download", false);
        prefs.put("savefile.default_directory", downloadPath);
        prefs.put("printing.print_preview_sticky_settings.appState",
                "{\"recentDestinations\":[{\"id\":\"Save as PDF\",\"origin\":\"local\",\"account\":\"\"}],"
                + "\"selectedDestinationId\":\"Save as PDF\",\"version\":2,"
                + "\"isCssBackgroundEnabled\":true,\"isHeaderFooterEnabled\":false}");
        prefs.put("printing.default_destination_selection_rules",
                "{\"kind\":\"local\",\"namePattern\":\"Save as PDF\"}");
        options.setExperimentalOption("prefs", prefs);
        options.addArguments("--kiosk-printing");

        // Initialize the driver ONCE for the entire suite
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // Explicit wait instance for use in tests
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    @AfterSuite
    public void tearDownSuite() {
        if (driver != null) {
            driver.quit();
        }
    }
}