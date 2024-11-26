package pl.business.process.automation.fileDownloader.chromeDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Base64;
import java.util.concurrent.TimeUnit;

public class WebDriverInitializer {

    private static final String LOGIN_URL = "user_url";
    private static final String ENCODED_PASSWORD = "user_password";
    private static final String USER_EMAIL = "user_email";

    private static final String CHROME_DRIVER_PATH = "C:\\Users\\Desktop\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe";

    public static WebDriver initializeWebDriver() {
        setChromeDriverProperty();
        ChromeOptions options = new ChromeOptions();
        WebDriver driver = new ChromeDriver(options);
        configureDriverTimeouts(driver);
        performLogin(driver);
        return driver;
    }

    private static void setChromeDriverProperty() {
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
    }

    private static void configureDriverTimeouts(WebDriver driver) {
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
    }

    private static void performLogin(WebDriver driver) {
        login(driver, USER_EMAIL, decodePassword());
    }

    public static void login(WebDriver driver, String userEmail, String password) {
        driver.get(LOGIN_URL);
        enterTextInField(driver, "user_login", userEmail);
        enterTextInField(driver, "user_password", password);
        submitPassword(driver, "user_password", password);
    }

    private static String decodePassword() {
        byte[] decodedBytes = Base64.getDecoder().decode(WebDriverInitializer.ENCODED_PASSWORD);
        return new String(decodedBytes);
    }

    private static void enterTextInField(WebDriver driver, String fieldId, String text) {
        WebElement field = driver.findElement(By.id(fieldId));
        field.clear();
        field.sendKeys(text);
    }

    private static void submitPassword(WebDriver driver, String fieldId, String password) {
        WebElement passwordField = driver.findElement(By.id(fieldId));
        passwordField.clear();
        passwordField.sendKeys(password);
        passwordField.sendKeys(Keys.ENTER);
    }
}
