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

    protected WebDriver driver;
    private final String browser;

    MainPage mainPage;
    OrderMakePage orderMakePage;

    public BaseTest(String browser) {
        this.browser = browser;
    }

    @Before
    public void start() {
        if ("chrome".equalsIgnoreCase(browser)) {
            WebDriverManager.chromedriver().setup();  // ← сначала setup
            driver = new ChromeDriver();               // ← потом создание
        } else if ("firefox".equalsIgnoreCase(browser)) {
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
        } else {
            throw new IllegalArgumentException("Неподдерживаемый браузер: " + browser);
        }

        try {
            driver.manage().window().maximize();
        } catch (Exception e) {
            System.err.println("Window maximize failed: " + e.getMessage());
        }
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        mainPage = new MainPage(driver, wait);
        orderMakePage = new OrderMakePage(driver);
    }

    @After
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    public void openPage() {
        if (mainPage != null) {
            mainPage.open();
            mainPage.acceptCookies();
        }
    }

    public WebDriver getDriver() {
        return driver;
    }
}