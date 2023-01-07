package com.qa.tests;

import com.google.common.collect.ImmutableMap;
import com.qa.BaseTest;
import com.qa.pages.LoginPage;
import com.qa.pages.ProductPage;
import com.qa.utils.TestUtils;
import io.appium.java_client.AppiumDriver;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

import static com.google.auto.common.AnnotationValues.getStrings;
import static org.testng.Assert.assertEquals;

public class LoginTests extends BaseTest {
    LoginPage loginPage;
    ProductPage productPage;
    TestUtils utils = new TestUtils();
    JSONObject loginUsers;

    @BeforeClass
    public void beforeClass() throws IOException {
        InputStream datais = null;
        try {
            String dataFileName = "data/loginUsers";
            datais = getClass().getClassLoader().getResourceAsStream(dataFileName);
            JSONTokener tokener = new JSONTokener(datais);
            loginUsers = new JSONObject(tokener);
        } catch(Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if(datais != null) {
                datais.close();
            }
        }
        closeApp();
        launchApp();
    }
    @AfterClass
    public void afterClass() {

    }
    @BeforeMethod
    public void beforeMethod(Method m) {
        utils.log().info("\n" + "****** starting test:" + m.getName() + "******" + "\n");
        loginPage = new LoginPage();
        productPage = new ProductPage();
    }
    @AfterMethod
    public void afterMethod() {

    }

    @Test
    public void invalidUserName() {
        loginPage.enterUserName(loginUsers.getJSONObject("invalidUser").getString("username"));
        loginPage.enterPassword(loginUsers.getJSONObject("invalidUser").getString("password"));
        loginPage.pressLoginBtn();

        String actualErrTxt = loginPage.getErrTxt();
        String expectedErrTxt = "Username and password do not match any user in this service.";

        Assert.assertEquals(actualErrTxt, expectedErrTxt);
    }

    @Test
    public void invalidPassword() {
        loginPage.enterUserName(loginUsers.getJSONObject("invalidPassword").getString("username"));
        loginPage.enterPassword(loginUsers.getJSONObject("invalidPassword").getString("password"));
        loginPage.pressLoginBtn();


        String actualErrTxt = loginPage.getErrTxt();
        String expectedErrTxt = "Username and password do not match any user in this service.";
        Assert.assertEquals(actualErrTxt, expectedErrTxt);
    }

    @Test
    public void succesfulLogin() {
        loginPage.scrollStandartUser();
        loginPage.pressStandartUser();
        loginPage.pressLoginBtn();

        String actualProductTittle = productPage.getTitle();
        String expectedProductTittle = "PRODUCTS";

        Assert.assertEquals(actualProductTittle, expectedProductTittle);
    }
}
