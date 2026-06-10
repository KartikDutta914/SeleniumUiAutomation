package com.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

/**
 * Page Object representing Amazon's Home Page.
 */
public class AmazonHomePage extends BasePage {

    // Locators
    private final By locationButton = By.id("nav-global-location-popover-link");
    private final By zipInput = By.id("GLUXZipUpdateInput");
    private final By zipApplyButton = By.cssSelector("#GLUXZipUpdate input");
    private final By doneButton = By.name("glowDoneButton");
    private final By searchField = By.id("twotabsearchtextbox");
    private final By searchSubmit = By.id("nav-search-submit-button");

    public AmazonHomePage(WebDriver driver) {
        super(driver);
    }

    /**
     * Changes the delivery location using a ZIP code.
     * @param pincode ZIP code string (e.g., "10001")
     */
    public void changePincode(String pincode) {
        logger.info("Initiating pincode change to: {}", pincode);
        elementUtils.click(locationButton, 10);
        
        // Wait for zip input to load and type the pincode
        elementUtils.sendKeys(zipInput, pincode, 10);
        
        // Click apply
        elementUtils.click(zipApplyButton, 10);
        
        // Click Done to save/confirm and reload
        try {
            // Wait a moment for "Done" button to be clickable
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.elementToBeClickable(doneButton)).click();
            logger.info("Clicked 'Done' button for location update");
        } catch (Exception e) {
            logger.warn("Done button not clickable or missing, attempting to dismiss via page reload: {}", e.getMessage());
            driver.navigate().refresh();
        }

        // Wait for page reload to complete and ensure home page search field is visible again
        elementUtils.waitForElementToBeVisible(searchField, 10);
        logger.info("Pincode changed successfully");
    }

    /**
     * Searches for a product.
     * @param product name of the product
     * @return AmazonSearchResultsPage page object
     */
    public AmazonSearchResultsPage searchProduct(String product) {
        logger.info("Searching for product: {}", product);
        elementUtils.sendKeys(searchField, product, 10);
        elementUtils.click(searchSubmit, 10);
        return new AmazonSearchResultsPage(driver);
    }
}
