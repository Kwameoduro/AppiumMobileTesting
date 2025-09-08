package com.mobile.tests.test;

import com.mobile.tests.base.BaseTest;
import com.mobile.tests.pages.LoginPage;
import com.mobile.tests.pages.ProductsPage;
import com.mobile.tests.utils.TestDataUtils;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.qameta.allure.Description;
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
                " Login Page was not displayed before starting the test.");
    }

    @Test(groups = {"smoke", "login"}, description = "Verify user can login successfully with valid credentials")
    @Story("Login with valid credentials")
    @Description("User should be able to log in with valid credentials and navigate to the Products page.")
    @Severity(SeverityLevel.CRITICAL)
    public void testValidLogin() {
        Map<String, String> data = TestDataUtils.getNestedMap(DATA_FILE, "validLogin");

        loginPage.login(data.get("username"), data.get("password"));

        ProductsPage productsPage = new ProductsPage(driver);
        Assert.assertTrue(productsPage.isPageDisplayed(),
                " Products page was NOT displayed after valid login.");
    }

    @Test(groups = {"regression", "login"}, description = "Verify that an error message is displayed for invalid login attempts")
    @Story("Invalid login attempt")
    @Description("User should see an error message when logging in with invalid credentials.")
    @Severity(SeverityLevel.CRITICAL)
    public void testInvalidLogin() {
        Map<String, String> credentials = TestDataUtils.getNestedMap(DATA_FILE, "invalidLogin");
        String username = credentials.get("username");
        String password = credentials.get("password");
        String expectedError = credentials.get("expectedError");

        loginPage.login(username, password);

        String actualError = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(driver -> {
                    String text = loginPage.getErrorMessage().trim();
                    return text.isEmpty() ? null : text;
                });

        Assert.assertNotNull(actualError, " No error message displayed for invalid login.");
        Assert.assertTrue(actualError.contains(expectedError),
                String.format(" Invalid login error message mismatch. Expected to contain: '%s', but was: '%s'", expectedError, actualError));
    }

    @Test(groups = {"regression", "login"}, description = "Verify locked-out user cannot login")
    @Story("Locked-out user login")
    @Description("User should not be able to log in if the account is locked out.")
    @Severity(SeverityLevel.CRITICAL)
    public void testLockedOutUser() {
        Map<String, String> data = TestDataUtils.getNestedMap(DATA_FILE, "lockedOutUser");

        loginPage.login(data.get("username"), data.get("password"));

        String errorMsg = loginPage.getErrorMessage().trim();
        Assert.assertFalse(errorMsg.isEmpty(), " No error message displayed for locked-out user.");
        Assert.assertEquals(errorMsg, data.get("expectedError"),
                " Locked-out user error message mismatch.");
    }

    @Test(groups = {"regression", "login"}, description = "Verify login fails when username is empty")
    @Story("Empty username login")
    @Description("Login should fail and display an error when the username field is empty.")
    @Severity(SeverityLevel.NORMAL)
    public void testEmptyUsername() {
        Map<String, String> data = TestDataUtils.getNestedMap(DATA_FILE, "emptyUsername");

        loginPage.login(data.get("username"), data.get("password"));

        String errorMsg = loginPage.getEmptyUsernameError();

        Assert.assertFalse(errorMsg.isEmpty(), " No error message displayed when username is empty.");
        Assert.assertEquals(errorMsg, data.get("expectedError"),
                " Empty username error message mismatch.");
    }

    @Test(groups = {"regression", "login"}, description = "Verify login fails when password is empty")
    @Story("Empty password login")
    @Description("Login should fail and display an error when the password field is empty.")
    @Severity(SeverityLevel.CRITICAL)
    public void testEmptyPassword() {
        Map<String, String> data = TestDataUtils.getNestedMap(DATA_FILE, "emptyPassword");

        loginPage.login(data.get("username"), data.get("password"));

        String errorMsg = loginPage.getEmptyPasswordError();

        Assert.assertFalse(errorMsg.isEmpty(), " No error message displayed when password is empty.");
        Assert.assertEquals(errorMsg, data.get("expectedError"),
                " Empty password error message mismatch.");
    }

    @Test(groups = {"smoke", "login"}, description = "Verify user can logout successfully after login")
    @Story("Logout after login")
    @Description("User should be able to log out successfully and return to the login page.")
    @Severity(SeverityLevel.NORMAL)
    public void testLogout() {
        Map<String, String> loginData = TestDataUtils.getNestedMap(DATA_FILE, "validLogin");
        loginPage.login(loginData.get("username"), loginData.get("password"));

        ProductsPage productsPage = new ProductsPage(driver);
        Assert.assertTrue(productsPage.isPageDisplayed(),
                " Products page was NOT displayed after valid login.");

        productsPage.logout();

        Assert.assertTrue(loginPage.isPageDisplayed(),
                " Login page was NOT displayed after logout.");
    }
}
