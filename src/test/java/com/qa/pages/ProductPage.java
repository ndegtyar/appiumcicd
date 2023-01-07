package com.qa.pages;

import com.qa.MenuPage;
import com.qa.utils.TestUtils;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;

public class ProductPage extends MenuPage {
    TestUtils utils = new TestUtils();

    @AndroidFindBy (xpath = "//android.view.ViewGroup[@content-desc=\"test-Cart drop zone\"]/android.view.ViewGroup/android.widget.TextView")
    @iOSXCUITFindBy(xpath ="//XCUIElementTypeOther[@name=\"test-Toggle\"]/parent::*[1]/preceding-sibling::*[1]")
    private WebElement productTitleTxt;

    @AndroidFindBy (xpath = "(//android.widget.TextView[@content-desc=\"test-Item title\"])[1]")
    @iOSXCUITFindBy (xpath = "(//XCUIElementTypeStaticText[@name=\"test-Item title\"])[1]")
    private WebElement SLBTitle;

    @AndroidFindBy (xpath = "(//android.widget.TextView[@content-desc=\"test-Price\"])[1]")
    @iOSXCUITFindBy (xpath = "(//XCUIElementTypeStaticText[@name=\"test-Price\"])[1]")
    private WebElement SLBPrice;

    public String getTitle() {
        return getText(productTitleTxt);
    }

    public String getSLBTitle() {
        return getText(SLBTitle);
    }

    public String getSLBPrice() {
        return getText(SLBPrice);
    }

    public ProductDetailsPage pressSLBTitle() {
        click(SLBTitle);
        return new ProductDetailsPage();
    }
}
