package com.automation.pages;

import com.automation.utils.ElementUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

/**
 * BasePage class serving as the parent of all page objects.
 */
public class BasePage {

    protected WebDriver driver;
    protected ElementUtils elementUtils;
    protected static final Logger logger = LogManager.getLogger(BasePage.class);

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.elementUtils = new ElementUtils(driver);
    }
}
