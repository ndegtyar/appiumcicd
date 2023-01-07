package com.qa;

import com.qa.pages.SettingsPage;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class MenuPage extends BaseTest{

    @AndroidFindBy (accessibility = "test-Menu") private WebElement settingsBtn;
    public SettingsPage pressSettingsBtn() {
        click(settingsBtn);
        return new SettingsPage();
    }
}
