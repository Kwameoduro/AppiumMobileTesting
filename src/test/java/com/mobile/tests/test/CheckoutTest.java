package com.mobile.tests.test;



import com.mobile.tests.base.BaseTest;
import com.mobile.tests.pages.LoginPage;
import com.mobile.tests.pages.ProductsPage;
import com.mobile.tests.pages.CartPage;
import com.mobile.tests.pages.CheckoutInformationPage;
import com.mobile.tests.pages.CheckoutOverviewPage;
import com.mobile.tests.pages.CheckoutCompletePage;
import com.mobile.tests.utils.TestDataUtils;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class CheckoutTest extends BaseTest {

    private static final String DATA_FILE = "testdata/checkoutData.json";

    private LoginPage loginPage;
    private ProductsPage productsPage;
    private CartPage cartPage;
    private CheckoutInformationPage checkoutInformationPage;
    private CheckoutOverviewPage checkoutOverviewPage;
    private CheckoutCompletePage checkoutCompletePage;

    @BeforeMethod
    public void setUpPages() {
        loginPage = new LoginPage(driver);
        productsPage = new ProductsPage(driver);
        cartPage = new CartPage(driver);
        checkoutInformationPage = new CheckoutInformationPage(driver);
        checkoutOverviewPage = new CheckoutOverviewPage(driver);
        checkoutCompletePage = new CheckoutCompletePage(driver);
    }

    @Test(description = "Verify end-to-end checkout flow from cart to order completion")
    public void testEndToEndCheckoutFlow() {
        // Step 1: Login
        Map<String, String> validUser = TestDataUtils.getNestedMap(DATA_FILE, "validUser");
        loginPage.login(validUser.get("username"), validUser.get("password"));

        // Step 2: Add product(s) to cart
        productsPage.addFirstProductToCart();
        productsPage.goToCart();
        Assert.assertFalse(cartPage.getCartItems().isEmpty(),
                "❌ Cart should contain at least one product before checkout.");

        // Step 3: Proceed to Checkout Information
        cartPage.proceedToCheckout();
        Assert.assertTrue(checkoutInformationPage.isPageDisplayed(),
                "❌ Checkout Information page not displayed.");

        // Step 4: Fill in customer info
        Map<String, String> customerInfo = TestDataUtils.getNestedMap(DATA_FILE, "customerInfo");
        checkoutInformationPage.enterFirstName(customerInfo.get("firstName"));
        checkoutInformationPage.enterLastName(customerInfo.get("lastName"));
        checkoutInformationPage.enterPostalCode(customerInfo.get("postalCode"));
        checkoutInformationPage.continueToOverview();

        // Step 5: Verify Overview Page
        Assert.assertTrue(checkoutOverviewPage.isPageDisplayed(),
                "❌ Checkout Overview page not displayed.");

        // Wait until overview items are visible
        List<WebElement> overviewItems = checkoutOverviewPage.getOverviewItems();
//        Assert.assertFalse(overviewItems.isEmpty(),
//                "❌ No items displayed in checkout overview.");

        // Step 6: Finish Checkout
        checkoutOverviewPage.finishCheckout();

        // Wait until checkout complete page is displayed
        Assert.assertTrue(checkoutCompletePage.isPageDisplayed(),
                "❌ Checkout Complete page not displayed.");

        // Step 7: Verify Order Confirmation
        String expectedMessage = TestDataUtils.getData(DATA_FILE, "confirmationMessage");
        String actualMessage = checkoutCompletePage.getConfirmationMessage();
        Assert.assertEquals(actualMessage, expectedMessage,
                "❌ Confirmation message mismatch.");
    }
}
