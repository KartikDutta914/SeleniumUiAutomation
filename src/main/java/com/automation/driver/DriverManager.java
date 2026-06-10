package com.automation.driver;

import org.openqa.selenium.WebDriver;

/**
 * Thread-safe DriverManager to support parallel execution using ThreadLocal.
 */
public class DriverManager {

    private DriverManager() {
        // Private constructor to prevent instantiation
    }

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    /**
     * Get the thread-local instance of WebDriver.
     * @return WebDriver instance for the current thread.
     */
    public static WebDriver getDriver() {
        return driver.get();
    }

    /**
     * Set the thread-local instance of WebDriver.
     * @param driverInstance WebDriver to associate with current thread.
     */
    public static void setDriver(WebDriver driverInstance) {
        driver.set(driverInstance);
    }

    /**
     * Remove the thread-local instance of WebDriver to prevent memory leaks.
     */
    public static void unloadDriver() {
        driver.remove();
    }
}
