package com.qa.tests;

import com.qa.BaseTest;
import com.qa.pages.LoginPage;
import com.qa.pages.ProductDetailsPage;
import com.qa.pages.ProductPage;
import com.qa.pages.SettingsPage;
import com.qa.ui.DeepLink;
import io.appium.java_client.AppiumDriver;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

public class ProductTests extends BaseTest {
    LoginPage loginPage;
    ProductPage productPage;
    SettingsPage settingsPage;
    ProductDetailsPage productDetailsPage;
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
        System.out.println("\n" + "***** starting test: " + m.getName() + "*****" + "\n");
        loginPage = new LoginPage();
        productPage = new ProductPage();

        DeepLink.OpenAppWith("swaglabs://swag-overview/0,1");
        ProductPage productPage = new ProductPage();
    }
    @AfterMethod
    public void afterMethod() {
        settingsPage = productPage.pressSettingsBtn();
        loginPage = settingsPage.pressLogoutBtn();
    }

    @Test
    public void validateProductOnProductsPage() {

        String actualProductTittle = productPage.getSLBTitle();
        String expectedProductTittle = "Sauce Labs Backpack";

        String actualProductPrice = productPage.getSLBPrice();
        String expectedProductPrice = "$29.99";

        Assert.assertEquals(actualProductTittle, expectedProductTittle);
    }

    @Test
    public void validateProductOnProductsDetailsPage() {
        productDetailsPage = productPage.pressSLBTitle();

        String actualProductTittle = productDetailsPage.getSLBTitle();
        String expectedProductTittle = "Sauce Labs Backpack";

        String actualProductTxt = productDetailsPage.getSLBTxt();
        String expectedProductTxt = "carry.allTheThings() with the sleek, streamlined Sly Pack that melds uncompromising style with unequaled laptop and tablet protection.";

        productDetailsPage.scrollToSLBPrice();
//        productDetailsPage.isAddToCartBtnDisplayed();


        String actualProductPrice = productDetailsPage.getSLBPrice();
        String expectedProductPrice = "$29.99";

        Assert.assertEquals(actualProductTittle, expectedProductTittle);

//        productPage = productDetailsPage.pressBackToProductsBtn();
    }
}
