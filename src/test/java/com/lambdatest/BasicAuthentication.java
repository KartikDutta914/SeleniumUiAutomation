package com.lambdatest;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * LambdaTest base setup — configures RemoteWebDriver with LambdaTest
 * capabilities.
 */
public class BasicAuthentication {

    protected WebDriver driver;

    private static final String LT_USERNAME = System.getenv("LT_USERNAME");
    private static final String LT_ACCESS_KEY = System.getenv("LT_ACCESS_KEY");
    private static final String GRID_URL = "https://" + LT_USERNAME + ":" + LT_ACCESS_KEY
            + "@hub.lambdatest.com/wd/hub";

    @BeforeMethod
    public void setUp() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browserName", "Chrome");
        capabilities.setCapability("browserVersion", "latest");
        capabilities.setCapability("platformName", "Windows 10");

        HashMap<String, Object> ltOptions = new HashMap<>();
        ltOptions.put("build", "SeleniumUiAutomation Build");
        ltOptions.put("name", "LambdaTest Session");
        ltOptions.put("project", "SeleniumUiAutomation");
        ltOptions.put("w3c", true);
        capabilities.setCapability("LT:Options", ltOptions);

        driver = new RemoteWebDriver(new URL(GRID_URL), capabilities);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
