package com.automation.listeners;

import com.automation.driver.DriverManager;
import com.automation.reports.ExtentReportManager;
import com.aventstack.extentreports.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TestNG listener to link test execution with Log4j2 and ExtentReports.
 */
public class TestListener implements ITestListener {

    private static final Logger logger = LogManager.getLogger(TestListener.class);
    private static final Map<String, String> testPrices = new ConcurrentHashMap<>();

    /**
     * Stores extracted device price for final reporting.
     */
    public static void recordPrice(String deviceType, String price) {
        testPrices.put(deviceType, price);
    }

    @Override
    public void onStart(ITestContext context) {
        logger.info("Initializing Test Suite: {}", context.getName());
        ExtentReportManager.getExtentReports();
    }

    @Override
    public void onFinish(ITestContext context) {
        logger.info("Finishing Test Suite: {}", context.getName());
        ExtentReportManager.flush();

        // Print final reports to terminal at the very end of execution
        System.out.println("\n==============================================");
        System.out.println("          FINAL DEVICE PRICES REPORT          ");
        System.out.println("==============================================");
        String iphonePrice = testPrices.getOrDefault("iPhone", "Not Found");
        String galaxyPrice = testPrices.getOrDefault("Galaxy", "Not Found");
        System.out.println("iphone device price is : " + iphonePrice);
        System.out.println("Galaxy device price is " + galaxyPrice);
        System.out.println("==============================================\n");
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        logger.info("Starting Test: {}", testName);
        ExtentReportManager.createTest(testName);
        ExtentReportManager.getTest().log(Status.INFO, "Test '" + testName + "' started.");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        logger.info("Test Passed: {}", testName);
        ExtentReportManager.getTest().log(Status.PASS, "Test '" + testName + "' passed.");
        ExtentReportManager.unloadTest();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        logger.error("Test Failed: {}", testName);
        logger.error(result.getThrowable());

        ExtentReportManager.getTest().log(Status.FAIL, "Test '" + testName + "' failed.");
        ExtentReportManager.getTest().log(Status.FAIL, result.getThrowable());

        // Capture screenshot and embed as Base64 inline
        try {
            if (DriverManager.getDriver() != null) {
                String base64Screenshot = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BASE64);
                ExtentReportManager.getTest().addScreenCaptureFromBase64String(base64Screenshot, "Failure Screenshot");
                logger.info("Screenshot attached to report for failed test: {}", testName);
            }
        } catch (Exception e) {
            logger.error("Failed to capture screenshot for failed test: {}", testName, e);
        }
        
        ExtentReportManager.unloadTest();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        logger.warn("Test Skipped: {}", testName);
        ExtentReportManager.getTest().log(Status.SKIP, "Test '" + testName + "' skipped.");
        ExtentReportManager.unloadTest();
    }
}
