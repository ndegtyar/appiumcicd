package com.qa.pages;

import com.qa.MenuPage;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;

public class ProductDetailsPage extends MenuPage {

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Description\"]/android.widget.TextView[1]\n" +
            "")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[@name=\"test-Description\"]/child::XCUIElementTypeStaticText[1]")
    private WebElement SLBTitle;

    @AndroidFindBy (xpath = "//android.view.ViewGroup[@content-desc=\"test-Description\"]/android.widget.TextView[2]"
            + "")
    @iOSXCUITFindBy (xpath = "//XCUIElementTypeOther[@name=\"test-Description\"]/child::XCUIElementTypeStaticText[2]")
    private WebElement SLBTxt;

    @AndroidFindBy (accessibility = "test-Price")
    private WebElement SLBPrice;

    @AndroidFindBy (accessibility = "test-BACK TO PRODUCTS")
    @iOSXCUITFindBy (id = "test-BACK TO PRODUCTS")
    private WebElement backToProductsBtn;

    @iOSXCUITFindBy (id = "test-ADD TO CART") private WebElement addToCartBtn;

    public String getSLBTitle() {
        return getText(SLBTitle);
    }

    public String getSLBTxt() {
        return getText(SLBTxt);
    }

    public String getSLBPrice() {
        return getText(SLBPrice);
    }

    public ProductPage pressBackToProductsBtn() {
        click(backToProductsBtn);
        return new ProductPage();
    }

    public ProductDetailsPage scrollToSLBPrice() {
        scrollToProductDetailElement();
        return this;
    }
    public void scrollPage() {
        iOSScrollToElement();
    }

    public Boolean isAddToCartBtnDisplayed() {
        return addToCartBtn.isDisplayed();
    }

}
