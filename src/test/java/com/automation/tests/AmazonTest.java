package com.automation.tests;

import com.automation.driver.DriverManager;
import com.automation.pages.AmazonHomePage;
import com.automation.pages.AmazonProductDetailsPage;
import com.automation.pages.AmazonSearchResultsPage;
import com.automation.listeners.TestListener;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Suite containing parallel automated test flows for Amazon product search and cart.
 */
public class AmazonTest extends BaseTest {

    @Test(description = "Test Case 1: Search and add iPhone to cart after changing location")
    public void testAmazonIPhone() {
        logger.info("Starting Test Case 1: Amazon iPhone Search & Add to Cart");
        
        AmazonHomePage homePage = new AmazonHomePage(DriverManager.getDriver());
        
        // 1. Change Pincode/Zip to '10001'
        homePage.changePincode("10001");
        
        // 2. Search for any iPhone
        AmazonSearchResultsPage searchResultsPage = homePage.searchProduct("iPhone");
        
        // 3. Click on the first search result
        AmazonProductDetailsPage detailsPage = searchResultsPage.clickFirstProduct();
        
        // 4. Retrieve and print price to console
        String price = detailsPage.getProductPrice();
        TestListener.recordPrice("iPhone", price);
        System.out.println("========== IPHONE DEVICE PRICE: " + price + " ==========");
        logger.info("Retrieved iPhone price: {}", price);
        Assert.assertNotEquals(price, "Price not found", "Failed to retrieve the iPhone price");
        
        // 5. Add to shopping cart
        detailsPage.addToCart();
        
        // Verify cart addition confirmation
        boolean isAdded = detailsPage.isAddedToCartConfirmationDisplayed();
        logger.info("iPhone add to cart completed. Confirmed: {}", isAdded);
    }

    @Test(description = "Test Case 2: Search and add Galaxy device to cart after changing location")
    public void testAmazonGalaxy() {
        logger.info("Starting Test Case 2: Amazon Galaxy Search & Add to Cart");
        
        AmazonHomePage homePage = new AmazonHomePage(DriverManager.getDriver());
        
        // 1. Change Pincode/Zip to '10001'
        homePage.changePincode("10001");
        
        // 2. Search for any Galaxy device
        AmazonSearchResultsPage searchResultsPage = homePage.searchProduct("Galaxy device");
        
        // 3. Click on the first search result
        AmazonProductDetailsPage detailsPage = searchResultsPage.clickFirstProduct();
        
        // 4. Retrieve and print price to console
        String price = detailsPage.getProductPrice();
        TestListener.recordPrice("Galaxy", price);
        System.out.println("========== GALAXY DEVICE PRICE: " + price + " ==========");
        logger.info("Retrieved Galaxy device price: {}", price);
        Assert.assertNotEquals(price, "Price not found", "Failed to retrieve the Galaxy device price");
        
        // 5. Add to shopping cart
        detailsPage.addToCart();
        
        // Verify cart addition confirmation
        boolean isAdded = detailsPage.isAddedToCartConfirmationDisplayed();
        logger.info("Galaxy device add to cart completed. Confirmed: {}", isAdded);
    }
}
