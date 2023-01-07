package com.qa.pages;

import com.qa.BaseTest;
import com.qa.utils.TestUtils;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class LoginPage extends BaseTest {
    AppiumDriver driver;
    TestUtils utils = new TestUtils();

    @AndroidFindBy (accessibility = "test-Username")
    @iOSXCUITFindBy(id = "test-Username")
    private WebElement usernameTxtFld;

    @AndroidFindBy (accessibility = "test-Password")
    @iOSXCUITFindBy (id = "test-Password")
    private WebElement passwordTxtFld;

    @AndroidFindBy (accessibility = "test-LOGIN")
    @iOSXCUITFindBy (id = "test-LOGIN")
    private WebElement loginBtn;

    @AndroidFindBy (xpath = "//android.view.ViewGroup[@content-desc=\"test-Error message\"]/android.widget.TextView")
    @iOSXCUITFindBy (xpath = "//XCUIElementTypeOther[@name=\"test-Error message\"]/child::XCUIElementTypeStaticText")
    private WebElement errTxt;

    @iOSXCUITFindBy (xpath = "(//XCUIElementTypeStaticText[@name=\"iconIcon\"])[2]")
    private WebElement clearBtn;

    @AndroidFindBy (xpath = "//android.view.ViewGroup[@content-desc=\"test-standard_user\"]/android.widget.TextView")
    @iOSXCUITFindBy (id = "test-standard_user")
    private WebElement standartUserBtn;

    public LoginPage enterUserName(String username) {
        clear(usernameTxtFld);
        sendKeys(usernameTxtFld, username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        clear(passwordTxtFld);
        sendKeys(passwordTxtFld, password);
        return this;
    }

    public LoginPage scrollStandartUser() {
        scrollToElement();
        return this;
    }

    public LoginPage pressClearBtn() {
        click(clearBtn);
        return this;
    }

    public ProductPage pressLoginBtn() {
        click(loginBtn);
        return new ProductPage();
    }

    public LoginPage pressStandartUser() {
        click(standartUserBtn);
        return this;
    }

    public ProductPage login(String username, String password) {
        enterUserName(username);
        enterPassword(password);
        return pressLoginBtn();
    }

    public String getErrTxt() {
        return getText(errTxt);
    }
}
