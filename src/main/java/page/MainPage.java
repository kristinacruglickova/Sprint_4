package page;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class MainPage {
    public static final String URL = "https://qa-scooter.praktikum-services.ru/";

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By acceptBtn = By.id("rcc-confirm-button");
    private final By upOrderBtn = By.xpath(".//button[contains(@class, 'Button_Middle__1CSJM') and text()='Заказать']");
    private final By downOrderBtn = By.xpath(".//button[contains(@class, 'Button_Middle') and text() = 'Заказать']");
    private final By logoSakamoto = By.xpath(".//*[@alt='Scooter']");
    private final By logoYandex = By.xpath(".//*[@alt='Yandex']");
    private final String scrollIntoViewScript = "arguments[0].scrollIntoView();";

    public MainPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    /**
     * Кликает по кнопке заказа в верхней части страницы
     */
    public void clickUpOrderButton() {
        driver.findElement(upOrderBtn).click();
    }

    /**
     * Кликает по кнопке заказа в нижней части страницы (с прокруткой)
     */
    public void clickDownOrderButton() {
        WebElement button = driver.findElement(downOrderBtn);
        scrollDown(button);
        button.click();
    }

    /**
     * Прокручивает элемент в видимую область экрана
     * @param element элемент, к которому нужно прокрутить
     */
    private void scrollDown(WebElement element) {
        wait.until(d -> {
            ((JavascriptExecutor) d).executeScript(scrollIntoViewScript, element);
            return element.isDisplayed();
        });
    }

    /**
     * Кликает по логотипу Самоката
     */
    public void clickLogoSakamoto() {
        driver.findElement(logoSakamoto).click();
    }

    /**
     * Кликает по логотипу Яндекса
     */
    public void clickLogoYandex() {
        driver.findElement(logoYandex).click();
    }

    /**
     * Получает URL открытой во второй вкладке страницы
     * @return URL текущей вкладки
     */
    public String getUrlOpenedInSecondTab() {
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));

        Set<String> allWindows = driver.getWindowHandles();
        String currentWindow = driver.getWindowHandle();

        for (String window : allWindows) {
            if (!window.equals(currentWindow)) {
                driver.switchTo().window(window);
                // Ждём, пока страница загрузится (не будет about:blank)
                wait.until(d -> !Objects.equals(driver.getCurrentUrl(), "about:blank"));

                // Ждём завершения редиректов
                wait.until(d -> {
                    String currentUrl = driver.getCurrentUrl();
                    return !currentUrl.contains("yandex.ru") &&
                            !currentUrl.contains("sso");
                });
                break;
            }
        }
        return driver.getCurrentUrl();
    }

    /**
     * Принимает куки (кликает по кнопке согласия)
     */
    public void acceptCookies() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        driver.findElement(getAcceptBtn()).click();
    }

    /**
     * Открывает главную страницу сервиса
     */
    public void open() {
        driver.get(URL);
    }

    /**
     * Кликает по элементу с ожиданием кликабельности
     * @param button элемент кнопки для клика
     */
    public void clickButton(WebElement button) {
        wait.until(ExpectedConditions.elementToBeClickable(button)).click();
    }

    /**
     * Возвращает текущий URL страницы
     * @return текущий URL
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Возвращает локатор кнопки принятия куки
     * @return локатор кнопки согласия с куки
     */
    public By getAcceptBtn() {
        return acceptBtn;
    }

    public long getUrlYandex() {
        return 0;
    }
}
