package com.automation.tests;

import com.automation.driver.DriverFactory;
import com.automation.driver.DriverManager;
import com.automation.utils.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * BaseTest handles driver setup and teardown.
 * Supports both local browser execution and LambdaTest cloud grid,
 * switchable via execution.mode in config.properties.
 */
public class BaseTest {

    protected static final Logger logger = LogManager.getLogger(BaseTest.class);

    @BeforeMethod
    public void setUp() throws MalformedURLException {
        // Priority: -Dexecution.mode=lambdatest (command line) > config.properties
        String executionMode = System.getProperty("execution.mode",
                ConfigReader.getProperty("execution.mode") != null
                        ? ConfigReader.getProperty("execution.mode")
                        : "local");

        WebDriver driver;

        if (executionMode.equalsIgnoreCase("lambdatest")) {
            logger.info("Execution mode: LambdaTest Cloud Grid");
            driver = createLambdaTestDriver();
        } else {
            logger.info("Execution mode: Local Browser");
            String browser = ConfigReader.getProperty("browser");
            if (browser == null)
                browser = "chrome";
            boolean headless = Boolean.parseBoolean(ConfigReader.getProperty("headless"));
            driver = DriverFactory.createDriver(browser, headless);
        }

        DriverManager.setDriver(driver);
        driver.manage().window().maximize();

        String url = ConfigReader.getProperty("url");
        logger.info("Navigating to application URL: {}", url);
        driver.get(url);
    }

    /**
     * Creates a RemoteWebDriver connected to LambdaTest cloud grid.
     * LT_USERNAME and LT_ACCESS_KEY are read from environment variables.
     */
    private WebDriver createLambdaTestDriver() throws MalformedURLException {
        // Read credentials: -DLT_USERNAME on command line takes priority over env var
        String ltUsername = System.getProperty("LT_USERNAME", System.getenv("LT_USERNAME"));
        String ltAccessKey = System.getProperty("LT_ACCESS_KEY", System.getenv("LT_ACCESS_KEY"));

        if (ltUsername == null || ltAccessKey == null) {
            throw new RuntimeException(
                    "LambdaTest credentials not found.");
        }

        String gridUrl = "https://" + ltUsername + ":" + ltAccessKey + "@hub.lambdatest.com/wd/hub";

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browserName", ConfigReader.getProperty("lt.browser"));
        capabilities.setCapability("browserVersion", ConfigReader.getProperty("lt.browser.version"));
        capabilities.setCapability("platformName", ConfigReader.getProperty("lt.platform"));

        HashMap<String, Object> ltOptions = new HashMap<>();
        ltOptions.put("build", ConfigReader.getProperty("lt.build"));
        ltOptions.put("name", "Amazon Parallel Tests");
        ltOptions.put("project", ConfigReader.getProperty("lt.project"));
        ltOptions.put("w3c", true);
        capabilities.setCapability("LT:Options", ltOptions);

        logger.info("Connecting to LambdaTest hub: {}", gridUrl.replaceAll(":.*@", ":***@"));
        return new RemoteWebDriver(new URL(gridUrl), capabilities);
    }

    @AfterMethod
    public void tearDown() {
        logger.info("Tearing down WebDriver context...");
        WebDriver driver = DriverManager.getDriver();
        if (driver != null) {
            driver.quit();
            DriverManager.unloadDriver();
            logger.info("WebDriver quit successfully.");
        }
    }
}
