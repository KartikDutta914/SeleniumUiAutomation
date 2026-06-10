package com.automation.utils;

import java.time.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Utility class containing wrapper methods for Selenium interactions,
 * incorporating explicit waits for robustness.
 */
public class ElementUtils {

    private static final Logger logger = LogManager.getLogger(ElementUtils.class);
    private final WebDriver driver;

    public ElementUtils(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Waits for an element to be visible and returns it.
     */
    public WebElement waitForElementToBeVisible(By locator, int timeoutInSeconds) {
        logger.debug("Waiting for element to be visible: {}", locator);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Waits for an element to be clickable and returns it.
     */
    public WebElement waitForElementToBeClickable(By locator, int timeoutInSeconds) {
        logger.debug("Waiting for element to be clickable: {}", locator);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Clicks on an element after waiting for it to be clickable.
     */
    public void click(By locator, int timeoutInSeconds) {
        logger.info("Clicking on element: {}", locator);
        waitForElementToBeClickable(locator, timeoutInSeconds).click();
    }

    /**
     * Enters text into an element after waiting for it to be visible.
     */
    public void sendKeys(By locator, String text, int timeoutInSeconds) {
        logger.info("Typing '{}' into element: {}", text, locator);
        WebElement element = waitForElementToBeVisible(locator, timeoutInSeconds);
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Retrieves the text of an element after waiting for it to be visible.
     */
    public String getText(By locator, int timeoutInSeconds) {
        logger.debug("Getting text from element: {}", locator);
        return waitForElementToBeVisible(locator, timeoutInSeconds).getText();
    }

    /**
     * Checks if an element is displayed on the page.
     */
    public boolean isDisplayed(By locator, int timeoutInSeconds) {
        try {
            WebElement element = waitForElementToBeVisible(locator, timeoutInSeconds);
            boolean displayed = element.isDisplayed();
            logger.info("Element '{}' is displayed: {}", locator, displayed);
            return displayed;
        } catch (Exception e) {
            logger.warn("Element '{}' is not displayed or timed out: {}", locator, e.getMessage());
            return false;
        }
    }
}
