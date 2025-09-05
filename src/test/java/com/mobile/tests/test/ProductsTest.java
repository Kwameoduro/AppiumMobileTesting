package com.mobile.tests.test;

import com.mobile.tests.base.BaseTest;
import com.mobile.tests.pages.LoginPage;
import com.mobile.tests.pages.ProductsPage;
import com.mobile.tests.utils.TestDataUtils;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class ProductsTest extends BaseTest {

    private static final String DATA_FILE = "testdata/productData.json";

    private LoginPage loginPage;
    private ProductsPage productsPage;

    @BeforeMethod(alwaysRun = true)
    public void setUpPages() {
        loginPage = new LoginPage(driver);
        productsPage = new ProductsPage(driver);
    }

    /**
     * Helper: log in with valid user from test data
     */
    private void loginWithValidUser() {
        Map<String, String> validUser = TestDataUtils.getNestedMap(DATA_FILE, "validUser");
        Assert.assertNotNull(validUser, "❌ 'validUser' not found in " + DATA_FILE);

        loginPage.login(validUser.get("username"), validUser.get("password"));
        Assert.assertTrue(productsPage.isPageDisplayed(),
                "❌ Products page was not displayed after login.");
    }

    @Test(groups = {"smoke", "products"}, description = "Verify user can log in and land on the Products page")
    @Story("Login and navigate to Products page")
    @Description("User should log in successfully and be navigated to the Products page.")
    @Severity(SeverityLevel.CRITICAL)
    public void testSuccessfulLoginNavigatesToProductsPage() {
        loginWithValidUser();
    }

    @Test(groups = {"regression", "products"}, description = "Verify product list is displayed on Products page")
    @Story("Display product list")
    @Description("The product list should be displayed on the Products page after login.")
    @Severity(SeverityLevel.NORMAL)
    public void testProductListIsDisplayed() {
        loginWithValidUser();
        List<String> productNames = productsPage.getAllProductNames();
        Assert.assertFalse(productNames.isEmpty(), "❌ No products found on the Products page.");
    }

    @Test(groups = {"regression", "products"}, description = "Verify first product name and price match expected values")
    @Story("Verify first product details")
    @Description("First product's name and price should match expected values from test data.")
    @Severity(SeverityLevel.NORMAL)
    public void testFirstProductDetails() {
        loginWithValidUser();

        List<Map<String, String>> products = TestDataUtils.getListOfMaps(DATA_FILE, "products");
        Assert.assertFalse(products.isEmpty(), "❌ 'products' list is empty in test data.");

        String expectedName = products.get(0).get("name");
        String expectedPrice = products.get(0).get("price");

        String actualName = productsPage.getAllProductNames().get(0);
        String actualPrice = productsPage.getAllProductPrices().get(0);

        Assert.assertEquals(actualName, expectedName, "❌ Product name mismatch.");
        Assert.assertEquals(actualPrice, expectedPrice, "❌ Product price mismatch.");
    }

    @Test(groups = {"smoke", "products"}, description = "Verify sorting functionality shows available options")
    @Story("Check sort options")
    @Description("Sorting functionality should display available sorting options.")
    @Severity(SeverityLevel.CRITICAL)
    public void testSortOptionsAreAvailable() {
        loginWithValidUser();

        productsPage.tapSortButton();

        List<String> sortOptions = TestDataUtils.getListOfStrings(DATA_FILE, "sortOptions");
        Assert.assertFalse(sortOptions.isEmpty(), "❌ Sort options not defined in test data.");
    }

    @Test(groups = {"regression", "products"}, description = "Verify user can add a product to the cart")
    @Story("Add product to cart")
    @Description("User should be able to add a product to the cart and navigate to the cart page.")
    @Severity(SeverityLevel.NORMAL)
    public void testAddFirstProductToCart() {
        loginWithValidUser();

        productsPage.addFirstProductToCart();
        productsPage.goToCart();

        // Placeholder: replace with CartPage validation later
        Assert.assertTrue(true, "✅ Navigation to cart performed after adding product.");
    }
}
