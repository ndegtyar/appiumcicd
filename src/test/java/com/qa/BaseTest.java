package com.qa;

import com.qa.utils.TestUtils;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.InteractsWithApps;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.screenrecording.CanRecordScreen;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import org.apache.commons.codec.binary.Base64;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.tracing.Status;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.*;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static java.sql.DriverManager.getDriver;


public class BaseTest {
    public AppiumDriver getDriver() {
        return driver;
    }

    public static Properties getProps() {
        return props;
    }

    public static String getPlatform() {
        return platform;
    }

    public static void setDriver(AppiumDriver driver) {
        BaseTest.driver = driver;
    }

    public static void setProps(Properties props) {
        BaseTest.props = props;
    }

    public static void setPlatform(String platform) {
        BaseTest.platform = platform;
    }

    protected static AppiumDriver driver;
    protected static Properties props;
    protected static HashMap<String, String> strings = new HashMap<String, String>();
    protected static String platform;
    protected static String dateTime;
    protected static ThreadLocal <String> deviceName = new ThreadLocal<String>();
    private static AppiumDriverLocalService server;
    InputStream inputStream;
    InputStream stringsis;
    TestUtils utils;

    public BaseTest() {
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    @BeforeMethod
    public void beforeMethod() {
        ((CanRecordScreen) getDriver()).startRecordingScreen();
    }

    @AfterMethod
    public synchronized void afterMethod(ITestResult result) throws Exception {
        String media = ((CanRecordScreen) getDriver()).stopRecordingScreen();

        if(result.getStatus() == 2) {
            Map<String, String> params = result.getTestContext().getCurrentXmlTest().getAllParameters();
            String dirPath = "videos" + File.separator + params.get("platformName") + params.get("platformVersion") + "_" + params.get("deviceName")
                    + File.separator + getDateTime() + File.separator + result.getTestClass().getRealClass().getSimpleName();

            File videoDir = new File(dirPath);

            synchronized(videoDir){
                if(!videoDir.exists()) {
                    videoDir.mkdirs();
                }
            }

            try {
                FileOutputStream stream = new FileOutputStream(videoDir + File.separator + result.getName() + ".mp4");
                stream.write(Base64.decodeBase64(media));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @BeforeSuite
    public void beforeSuit() {
        server = getAppiumServerDegault();
        server.start();
        System.out.println("Appim server is start");
    }

    @AfterSuite
    public void afterSuit() {
        server.stop();
        System.out.println("Appim server is stop");
    }

    public AppiumDriverLocalService getAppiumServerDegault() {
        return AppiumDriverLocalService.buildDefaultService();
    }

    @Parameters({"platformName", "platformVersion", "deviceName"})
    @BeforeTest
    public void beforeTest(String platformName, String platformVersion, String deviceName) throws Exception {
        utils = new TestUtils();
        dateTime = utils.dateTime();
        platform = platformName;
        URL url;
        try {
            props = new Properties();
            String propFileName = "config.properties";
            String xmlFileName = "strings/strings.xml";
            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
            props.load(inputStream);

            stringsis = getClass().getClassLoader().getResourceAsStream(xmlFileName);
            strings = utils.parseStringXML(stringsis);

            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setCapability(MobileCapabilityType.PLATFORM_NAME, platformName);
            caps.setCapability(MobileCapabilityType.PLATFORM_VERSION, platformVersion);
            caps.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);

            switch(platformName) {
                case "Android":
                    caps.setCapability(MobileCapabilityType.UDID, "emulator-5554");
                    caps.setCapability(MobileCapabilityType.AUTOMATION_NAME, props.getProperty("androidAutomationName"));
                    caps.setCapability("appPackage", props.getProperty("androidAppPackage"));
                    caps.setCapability("appActivity", props.getProperty("androidAppActivity"));
                    URL androidAppUrl = getClass().getClassLoader().getResource(props.getProperty("androidAppLocation"));
                    caps.setCapability("app", androidAppUrl);
                    url = new URL(props.getProperty("appiumURL"));
                    driver = new AndroidDriver(url, caps);
                    break;
                case "iOS":
                    caps.setCapability(MobileCapabilityType.AUTOMATION_NAME, props.getProperty("iOSAutomationName"));
                    URL iOSappUrl = getClass().getClassLoader().getResource(props.getProperty("iOSAppLocation"));
                    //       caps.setCapability("app", iOSappUrl);
                    url = new URL(props.getProperty("appiumURL"));
                    driver = new IOSDriver(url, caps);
                    break;
                default:
                    throw new Exception("Invalid platform" + platformName);
            }


            String sessionID = driver.getSessionId().toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (stringsis != null) {
                stringsis.close();
            }
        }
    }

    public String getDeviceName() {
        return deviceName.get();
    }

    public String getDateTime() {
        return dateTime;
    }

    public void waitForVisibility(WebElement e) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestUtils.WAIT));
        wait.until(ExpectedConditions.visibilityOf(e));
    }

    public void clear(WebElement e) {
        waitForVisibility(e);
        e.clear();
    }

    public void click(WebElement e) {
        waitForVisibility(e);
        e.click();
    }

    public void sendKeys(WebElement e, String txt) {
        waitForVisibility(e);
        e.sendKeys(txt);
    }

    public String getText(WebElement e) {
        switch (platform) {
            case "Android":
                return getAttribute(e, "text");
            case "iOS":
                return getAttribute(e, "label");
        }
        return null;
    }

    public void closeApp() {
        switch(getPlatform()){
            case "Android":
                ((InteractsWithApps) getDriver()).terminateApp(getProps().getProperty("androidAppPackage"));
                break;
            case "iOS":
                ((InteractsWithApps) getDriver()).terminateApp(getProps().getProperty("iOSBundleId"));
        }
    }

    public void launchApp() {
        switch(getPlatform()){
            case "Android":
                ((InteractsWithApps) getDriver()).activateApp(getProps().getProperty("androidAppPackage"));
                break;
            case "iOS":
                ((InteractsWithApps) getDriver()).activateApp(getProps().getProperty("iOSBundleId"));
        }
    }

    public WebElement scrollToElement() {
        return driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector()" + ".description(\"test-Login\")).scrollIntoView("
                        + "new UiSelector().description(\"test-standard_user\"));"));
    }

    public WebElement scrollToProductDetailElement() {
        return driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector()" + ".description(\"test-Image Container\")).scrollIntoView("
                        + "new UiSelector().description(\"test-Price\"));"));
    }

    public void iOSScrollToElement() {
        //	  RemoteWebElement element = (RemoteWebElement)getDriver().findElement(By.name("test-ADD TO CART"));
//	  String elementID = element.getId();
        HashMap<String, String> scrollObject = new HashMap<String, String>();
//	  scrollObject.put("element", elementID);
        scrollObject.put("direction", "down");
//	  scrollObject.put("predicateString", "label == 'ADD TO CART'");
//	  scrollObject.put("name", "test-ADD TO CART");
//	  scrollObject.put("toVisible", "sdfnjksdnfkld");
        getDriver().executeScript("mobile:scroll", scrollObject);
    }

    public String getAttribute(WebElement e, String attribute) {
        waitForVisibility(e);
        return e.getAttribute(attribute);
    }

    @AfterTest
    public void afterTest() {
        driver.quit();
    }

}
