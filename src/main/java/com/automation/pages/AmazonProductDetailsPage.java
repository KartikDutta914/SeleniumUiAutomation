package com.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.List;

/**
 * Page Object representing Amazon's Product Details Page.
 */
public class AmazonProductDetailsPage extends BasePage {

    private final By productTitle = By.id("productTitle");

    // Candidate locators for retrieving the price (using visible wrapper elements)
    private final List<By> priceLocators = Arrays.asList(
            By.cssSelector("#corePriceDisplay_desktop_feature_div .a-price"),
            By.cssSelector("#corePrice_feature_div .a-price"),
            By.cssSelector(".priceToPay .a-price"),
            By.cssSelector(".priceToPay"),
            By.id("price_inside_buybox"),
            By.cssSelector(".a-price"),
            By.id("priceblock_ourprice")
    );

    // Candidate locators for adding to cart (covers different product page layouts)
    private final List<By> addToCartLocators = Arrays.asList(
            By.id("add-to-cart-button"),
            By.cssSelector("input[name='submit.add-to-cart']"),
            By.cssSelector(".a-button-input[aria-labelledby*='add-to-cart']"),
            By.xpath("//input[@id='add-to-cart-button'] | //button[@id='add-to-cart-button']")
    );

    // Locators for standard overlays (warranty upsell, protection plans, side sheets)
    private final List<By> dismissLocators = Arrays.asList(
            By.id("attachSiNoCoverage"),
            By.id("attachSiNoCoverage-announce"),
            By.xpath("//input[@aria-labelledby='attach-si-no-coverage-announce']"),
            By.id("attach-close_sideSheet-link"),
            By.cssSelector(".abb-intl-pop-close"),
            By.xpath("//button[contains(text(), 'No Thanks')]")
    );

    private final By cartSuccessBanner = By.cssSelector("#attach-display-products-status-text, .attach-post-action-confirmation-text, h1.a-size-medium-plus");

    public AmazonProductDetailsPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Extracts and retrieves the product price from the details page.
     * Tries several common locators for maximum robustness.
     * @return extracted price string, or "Price not found"
     */
    public String getProductPrice() {
        logger.info("Waiting for product details page to load (title visible)...");
        try {
            elementUtils.waitForElementToBeVisible(productTitle, 10);
            logger.info("Product details page title loaded successfully.");
        } catch (Exception e) {
            logger.warn("Timed out waiting for product title to load: {}", e.getMessage());
        }

        logger.info("Extracting product price...");
        for (By locator : priceLocators) {
            try {
                if (elementUtils.isDisplayed(locator, 2)) {
                    String price = elementUtils.getText(locator, 2);
                    if (price != null && !price.trim().isEmpty()) {
                        // Clean up formatted prices (e.g. fraction newline to dot)
                        String cleanPrice = price.replace("\n", ".").trim();
                        // If price contains multiple dollar signs or decimals, clean them up
                        if (cleanPrice.startsWith("$$")) {
                            cleanPrice = cleanPrice.substring(1);
                        }
                        logger.info("Found price using locator '{}': {}", locator, cleanPrice);
                        return cleanPrice;
                    }
                }
            } catch (Exception e) {
                // Ignore and try next locator
            }
        }
        logger.warn("All price locators failed to find product price.");
        return "Price not found";
    }

    /**
     * Clicks "Add to Cart" using multiple locator strategies and a JS fallback.
     * Scrolls the button into view before clicking to handle overlays.
     */
    public void addToCart() {
        logger.info("Attempting to click 'Add to Cart'...");
        WebElement cartButton = null;

        // Try each locator until we find one that is displayed
        for (By locator : addToCartLocators) {
            try {
                if (elementUtils.isDisplayed(locator, 3)) {
                    cartButton = elementUtils.waitForElementToBeVisible(locator, 3);
                    logger.info("Found Add to Cart button using locator: {}", locator);
                    break;
                }
            } catch (Exception e) {
                // Try next locator
            }
        }

        if (cartButton == null) {
            logger.warn("Standard Add to Cart locators failed. Attempting xpath fallback...");
            cartButton = elementUtils.waitForElementToBeVisible(
                    By.xpath("//*[contains(@id,'add-to-cart') or contains(@name,'add-to-cart')]"), 10);
        }

        // Scroll into view to ensure element is not hidden under sticky headers/overlays
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", cartButton);
            Thread.sleep(500);
        } catch (Exception e) {
            logger.warn("Could not scroll Add to Cart button into view: {}", e.getMessage());
        }

        // Try normal click first; fall back to JavaScript click if not interactable
        try {
            cartButton.click();
            logger.info("Add to Cart clicked successfully.");
        } catch (Exception e) {
            logger.warn("Normal click failed, attempting JavaScript click: {}", e.getMessage());
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", cartButton);
            logger.info("JavaScript click on Add to Cart executed.");
        }

        // Dismiss any optional coverage plans or accessory popups
        dismissOptionalPopups();
    }

    /**
     * Attempts to identify and click close/no-thanks buttons on potential upsell popups.
     */
    private void dismissOptionalPopups() {
        logger.info("Checking for optional protection or warranty popups...");
        for (By locator : dismissLocators) {
            try {
                if (elementUtils.isDisplayed(locator, 3)) {
                    logger.info("Detected overlay dismiss button: {}. Clicking it...", locator);
                    elementUtils.click(locator, 3);
                    // Add a tiny sleep to allow DOM animation to complete
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                // Suppress and continue
            }
        }
    }

    /**
     * Checks if the add-to-cart confirmation is displayed.
     * @return true if confirmation is visible
     */
    public boolean isAddedToCartConfirmationDisplayed() {
        try {
            for (By banner : Arrays.asList(cartSuccessBanner, By.id("nav-cart-count"))) {
                if (elementUtils.isDisplayed(banner, 5)) {
                    logger.info("Add-to-cart confirmation visible");
                    return true;
                }
            }
        } catch (Exception e) {
            // Ignore
        }
        return false;
    }
}
