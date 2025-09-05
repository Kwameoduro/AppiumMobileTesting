package com.mobile.tests.test;

import com.mobile.tests.base.BaseTest;
import com.mobile.tests.pages.CheckoutInformationPage;
import com.mobile.tests.pages.LoginPage;
import com.mobile.tests.pages.ProductsPage;
import com.mobile.tests.pages.CartPage;
import com.mobile.tests.utils.TestDataUtils;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class CartTest extends BaseTest {

    private static final String DATA_FILE = "testdata/cartData.json";

    private LoginPage loginPage;
    private ProductsPage productsPage;
    private CartPage cartPage;

    @BeforeMethod
    public void setUpPages() {
        loginPage = new LoginPage(driver);
        productsPage = new ProductsPage(driver);
        cartPage = new CartPage(driver);
    }

    @Test(description = "Verify adding a single product to the cart")
    public void testAddSingleProductToCart() {
        Map<String, String> validUser = TestDataUtils.getNestedMap(DATA_FILE, "validUser");
        loginPage.login(validUser.get("username"), validUser.get("password"));

        productsPage.addFirstProductToCart();
        productsPage.goToCart();

        Assert.assertFalse(cartPage.getCartItems().isEmpty(),
                "❌ Cart should not be empty after adding a product.");
    }


    @Test(description = "Verify removing a product decreases cart size")
    public void testRemoveProductFromCart() {
        Map<String, String> validUser = TestDataUtils.getNestedMap(DATA_FILE, "validUser");
        loginPage.login(validUser.get("username"), validUser.get("password"));

        productsPage.addFirstProductToCart();
        productsPage.goToCart();

        int initialSize = cartPage.getCartItems().size();
        cartPage.removeFirstItem();
        int newSize = cartPage.getCartItems().size();

        Assert.assertTrue(newSize < initialSize,
                "❌ Cart size should decrease after removing an item.");
    }

    @Test(description = "Verify proceeding to checkout navigates to Checkout Information page")
    public void testProceedToCheckout() {
        Map<String, String> validUser = TestDataUtils.getNestedMap(DATA_FILE, "validUser");
        loginPage.login(validUser.get("username"), validUser.get("password"));

        productsPage.addFirstProductToCart();
        productsPage.goToCart();

        CartPage cartPage = new CartPage(driver);
        cartPage.proceedToCheckout();

        CheckoutInformationPage checkoutPage = new CheckoutInformationPage(driver);
        Assert.assertTrue(checkoutPage.isPageDisplayed(),
                "❌ Did not navigate to Checkout Information page after clicking Checkout.");
    }


    @Test(description = "Verify empty cart shows correct message")
    public void testEmptyCartMessage() {
        Map<String, String> validUser = TestDataUtils.getNestedMap(DATA_FILE, "validUser");
        loginPage.login(validUser.get("username"), validUser.get("password"));

        productsPage.goToCart();
        CartPage cartPage = new CartPage(driver);

        Assert.assertTrue(cartPage.isEmptyCartMessageDisplayed(),
                "❌ Empty cart message not displayed.");
    }

    @Test(description = "Wise failing test: Verify user cannot checkout with an empty cart")
    public void testCannotCheckoutWithEmptyCart() {
        // Arrange: login as valid user
        Map<String, String> validUser = TestDataUtils.getNestedMap(DATA_FILE, "validUser");
        loginPage.login(validUser.get("username"), validUser.get("password"));

        // Act: go to cart without adding any products
        productsPage.goToCart();

        // Assert: checkout button should be disabled or checkout should not proceed
        // Currently, the bug allows checkout, so this test will fail
        Assert.assertFalse(cartPage.proceedToCheckout(),
                "❌ Checkout should NOT be allowed with an empty cart (known bug).");

        // Optional: verify empty cart message is displayed
        String expectedMessage = TestDataUtils.getData(DATA_FILE, "emptyCartMessage");
        Assert.assertEquals(cartPage.isEmptyCartMessageDisplayed(), expectedMessage,
                "❌ Empty cart message mismatch.");
    }
}
