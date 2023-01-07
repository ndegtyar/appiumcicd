package com.qa.pages;

import com.qa.BaseTest;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class SettingsPage extends BaseTest {

    @AndroidFindBy (accessibility = "test-LOGOUT") private WebElement logoutBtn;

    public LoginPage pressLogoutBtn() {
        click(logoutBtn);
        return new LoginPage();
    }
}
