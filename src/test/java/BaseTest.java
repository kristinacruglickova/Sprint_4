import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import page.MainPage;
import page.OrderMakePage;

import java.time.Duration;

public class BaseTest {
    private WebDriver driver;
    private final String browser;

    MainPage mainPage;
    OrderMakePage orderMakePage;

    public BaseTest(String browser) {
        this.browser = browser;
    }

    @Before
    public void start() {
        if (browser.equals("chrome")) {
            startBrowserChrome();
        } else if (browser.equals("firefox")) {
            startBrowserFirefox();
        }
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        mainPage = new MainPage(driver, wait);
        orderMakePage = new OrderMakePage(driver);
    }

    public void startBrowserChrome() {
        driver = new ChromeDriver();
        WebDriverManager.chromedriver().setup();
    }

    public void startBrowserFirefox() {
        driver = new FirefoxDriver();
        WebDriverManager.firefoxdriver().setup();
    }

    @After
    public void teardown() {
        //Закрыть браузер
        if (driver != null) {
            driver.quit();
        }
    }

    public void openPage() {
        mainPage.open();
        mainPage.acceptCookies();
    }
}
