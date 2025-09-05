package com.mobile.tests.test;

import com.mobile.tests.base.BaseTest;
import com.mobile.tests.pages.LoginPage;
import com.mobile.tests.pages.ProductsPage;
import com.mobile.tests.utils.TestDataUtils;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Map;

public class LoginTest extends BaseTest {

    private static final String DATA_FILE = "testdata/loginData.json";
    private LoginPage loginPage;

    @BeforeMethod(alwaysRun = true)
    public void initPages() {
        loginPage = new LoginPage(driver);
        Assert.assertTrue(loginPage.isLoginPageDisplayed(),
                "❌ Login Page was not displayed before starting the test.");
    }

    // -------------------------- Positive Test --------------------------
    @Test(groups = {"login"}, description = "Verify user can login successfully with valid credentials")
    public void testValidLogin() {
        Map<String, String> data = TestDataUtils.getNestedMap(DATA_FILE, "validLogin");

        loginPage.login(data.get("username"), data.get("password"));

        ProductsPage productsPage = new ProductsPage(driver);
        Assert.assertTrue(productsPage.isPageDisplayed(),
                "❌ Products page was NOT displayed after valid login.");
    }

    // -------------------------- Negative Tests --------------------------

    @Test(groups = {"login"}, description = "Verify that an error message is displayed for invalid login attempts")
    public void testInvalidLogin() {
        // Retrieve test data for invalid login
        Map<String, String> credentials = TestDataUtils.getNestedMap(DATA_FILE, "invalidLogin");
        String username = credentials.get("username");
        String password = credentials.get("password");
        String expectedError = credentials.get("expectedError");

        // Perform login attempt
        loginPage.login(username, password);

        // Wait for the error message to appear (up to 5 seconds)
        String actualError = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(driver -> {
                    String text = loginPage.getErrorMessage().trim();
                    return text.isEmpty() ? null : text; // Wait until non-empty
                });

        // Assert that error message is displayed
        Assert.assertNotNull(actualError, "❌ No error message displayed for invalid login.");

        // Assert that the error message contains expected text (more flexible than exact match)
        Assert.assertTrue(actualError.contains(expectedError),
                String.format("❌ Invalid login error message mismatch. Expected to contain: '%s', but was: '%s'", expectedError, actualError));
    }


    @Test(groups = {"login"}, description = "Verify locked-out user cannot login")
    public void testLockedOutUser() {
        Map<String, String> data = TestDataUtils.getNestedMap(DATA_FILE, "lockedOutUser");

        loginPage.login(data.get("username"), data.get("password"));

        String errorMsg = loginPage.getErrorMessage().trim();
        Assert.assertFalse(errorMsg.isEmpty(), "❌ No error message displayed for locked-out user.");
        Assert.assertEquals(errorMsg, data.get("expectedError"),
                "❌ Locked-out user error message mismatch.");
    }

    @Test(groups = {"login"}, description = "Verify login fails when username is empty")
    public void testEmptyUsername() {
        Map<String, String> data = TestDataUtils.getNestedMap(DATA_FILE, "emptyUsername");

        // Perform login with empty username
        loginPage.login(data.get("username"), data.get("password"));

        // Retrieve error message from dedicated getter
        String errorMsg = loginPage.getEmptyUsernameError();

        // Assertions
        Assert.assertFalse(errorMsg.isEmpty(), "❌ No error message displayed when username is empty.");
        Assert.assertEquals(errorMsg, data.get("expectedError"),
                "❌ Empty username error message mismatch.");
    }

    @Test(groups = {"login"}, description = "Verify login fails when password is empty")
    public void testEmptyPassword() {
        Map<String, String> data = TestDataUtils.getNestedMap(DATA_FILE, "emptyPassword");

        // Perform login with empty password
        loginPage.login(data.get("username"), data.get("password"));

        // Retrieve error message using dedicated getter
        String errorMsg = loginPage.getEmptyPasswordError();

        // Assertions
        Assert.assertFalse(errorMsg.isEmpty(), "❌ No error message displayed when password is empty.");
        Assert.assertEquals(errorMsg, data.get("expectedError"),
                "❌ Empty password error message mismatch.");
    }

    @Test(groups = {"login"}, description = "Verify user can logout successfully after login")
    public void testLogout() {
        // First, perform a valid login
        Map<String, String> loginData = TestDataUtils.getNestedMap(DATA_FILE, "validLogin");
        loginPage.login(loginData.get("username"), loginData.get("password"));

        ProductsPage productsPage = new ProductsPage(driver);
        Assert.assertTrue(productsPage.isPageDisplayed(),
                "❌ Products page was NOT displayed after valid login.");

        // Perform logout
        productsPage.logout(); // assuming you move the logout() method to ProductsPage

        // Verify that login page is displayed again
        Assert.assertTrue(loginPage.isPageDisplayed(),
                "❌ Login page was NOT displayed after logout.");
    }
}
