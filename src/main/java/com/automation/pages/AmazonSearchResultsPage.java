package com.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object representing search results page on Amazon.
 */
public class AmazonSearchResultsPage extends BasePage {

    // Robust locators to match first product link (either image link or title link)
    private final By firstResultLink = By.xpath("(//div[@data-component-type='s-search-result']//span[@data-component-type='s-product-image']//a | //div[@data-component-type='s-search-result']//h2/parent::a | //div[@data-component-type='s-search-result']//h2//a)[1]");
    private final By fallbackResultLink = By.xpath("(//div[contains(@class,'s-result-item')]//a[.//img])[1]");

    public AmazonSearchResultsPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Clicks on the first search result product.
     * @return AmazonProductDetailsPage page object
     */
    public AmazonProductDetailsPage clickFirstProduct() {
        logger.info("Selecting the first product from search results...");
        
        if (elementUtils.isDisplayed(firstResultLink, 10)) {
            elementUtils.click(firstResultLink, 10);
        } else {
            logger.warn("Primary search result locator not found. Attempting fallback locator.");
            elementUtils.click(fallbackResultLink, 10);
        }
        
        return new AmazonProductDetailsPage(driver);
    }
}
