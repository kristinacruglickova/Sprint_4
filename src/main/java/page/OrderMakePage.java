package page;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class OrderMakePage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // 🔹 Локаторы (упрощённые и проверенные)
    private final By modalDialog = By.cssSelector("div.Order_Modal__YZ-d3");
    private final By overlay = By.cssSelector("div.Order_Overlay__3KW-T");
    private final By overlays = By.xpath("//*[contains(@class,'Overlay')]");

    // Кнопка "Да" в модалке — по тексту в контейнере Buttons
    private final By confirmYesBtn = By.xpath("//div[contains(@class,'Order_Modal')]//div[contains(@class,'Order_Buttons')]//button[normalize-space()='Да']");

    // Кнопка "Посмотреть статус" и текст успеха
    private final By viewStatusBtn = By.xpath("//div[contains(@class,'Order_Modal')]//button[contains(., 'Посмотреть статус')]");
    private final By successText = By.xpath("//div[contains(@class,'Order_Modal')]//*[contains(., 'Заказ оформлен')]");

    // Форма: шаг 1
    private final By firstNameField = By.cssSelector("input[placeholder=\"* Имя\"]");
    private final By lastNameField = By.cssSelector("input[placeholder=\"* Фамилия\"]");
    private final By addressField = By.cssSelector("input[placeholder=\"* Адрес: куда привезти заказ\"]");
    private final By metroField = By.cssSelector("input[placeholder='* Станция метро']");
    private final By allStation = By.cssSelector("div.select-search__select li");
    private final By numberField = By.cssSelector("input[placeholder=\"* Телефон: на него позвонит курьер\"]");
    private final By nextBtn = By.xpath("//button[text()=\"Далее\"]");

    // Форма: шаг 2
    private final By dateField = By.cssSelector("input[placeholder='* Когда привезти самокат']");
    private final By rentalDropdown = By.className("Dropdown-placeholder");
    private final By rentalOptions = By.className("Dropdown-option");
    private final By blackCheck = By.xpath("//label[text()='чёрный жемчуг']/input[@type='checkbox']");
    private final By greyCheck = By.xpath("//label[text()='серая безысходность']/input[@type='checkbox']");

    // Финал
    private final By orderBtn = By.xpath("//div[contains(@class,'Buttons')]//button[contains(@class, 'Button_Middle__1CSJM') and contains(normalize-space(.), 'Заказать')]");

    // Проверки
    private final By successElement = By.xpath("//div[contains(@class,'Order_Modal')]//*[contains(., 'Заказ оформлен') or contains(., 'Посмотреть статус')]");
    private final By orderNumber = By.xpath("//div[contains(@class,'Order_Modal')]//p[contains(text(), '#')]");

    public OrderMakePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    // ==================== ШАГ 1: Данные ====================

    public void enterFirstName(String firstName) {
        wait.until(ExpectedConditions.presenceOfElementLocated(firstNameField)).sendKeys(firstName);
    }

    public void enterLastName(String lastName) {
        wait.until(ExpectedConditions.presenceOfElementLocated(lastNameField)).sendKeys(lastName);
    }

    public void enterAddress(String address) {
        wait.until(ExpectedConditions.presenceOfElementLocated(addressField)).sendKeys(address);
    }

    public void clickMetroField() {
        wait.until(ExpectedConditions.elementToBeClickable(metroField)).click();
    }

    public void selectStation(int index) {
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(allStation));
        List<WebElement> stations = driver.findElements(allStation);
        stations.get(index - 1).click();
    }

    public void enterNumber(String phone) {
        wait.until(ExpectedConditions.presenceOfElementLocated(numberField)).sendKeys(phone);
    }

    public void clickNextButton() {
        wait.until(ExpectedConditions.elementToBeClickable(nextBtn)).click();
    }

    // ==================== ШАГ 2: Аренда ====================

    public void selectDate(String date) {
        WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(dateField));
        el.sendKeys(date);
        el.sendKeys(Keys.RETURN);
    }

    public void clickRentalField() {
        wait.until(ExpectedConditions.elementToBeClickable(rentalDropdown)).click();
    }

    public void selectRentalPeriod(int index) {
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(rentalOptions));
        List<WebElement> options = driver.findElements(rentalOptions);
        options.get(index - 1).click();
    }

    public void selectScooterOption(String color) {
        By locator = "чёрный жемчуг".equals(color) ? blackCheck : greyCheck;
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    // ==================== КНОПКА "ЗАКАЗАТЬ" — ГЛАВНОЕ ИСПРАВЛЕНИЕ ====================

    /**
     * 🔹 Клик по кнопке "Заказать"
     * 1. Ждём исчезновения оверлея
     * 2. Скроллим к кнопке
     * 3. Пробуем обычный клик, если не вышло — JS
     */
    public void clickOrderButton() {
        System.out.println(">>> Clicking 'Заказать' button...");

        // 1. Ждём, пока оверлей не исчезнет (главная причина клика не срабатывал)
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(overlay));
            System.out.println(">>> Overlay disappeared");
        } catch (Exception e) {
            System.out.println(">>> No overlay found, continuing...");
        }

        // 2. Находим кнопку и ждём, пока она станет кликабельной
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(orderBtn));

        // 3. Скроллим к ней
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);

        // 4. Пробуем кликнуть
        try {
            button.click();
            System.out.println(">>> 'Заказать' clicked via Selenium");
        } catch (Exception e) {
            System.out.println(">>> Selenium click failed, using JS click");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
        }
    }

    // ==================== МОДАЛКА: клик по "Да" ====================

    /**
     * 🔹 Ждём модалку → кликаем "Да" → ждём смены контента
     */
    public void confirmAction() {
        System.out.println(">>> Waiting for modal dialog...");
        wait.until(ExpectedConditions.visibilityOfElementLocated(modalDialog));
        System.out.println(">>> Modal found");

        WebElement yesBtn = wait.until(ExpectedConditions.elementToBeClickable(confirmYesBtn));
        yesBtn.click();
        System.out.println(">>> 'Да' clicked");

        // Ждём успех
        wait.until(ExpectedConditions.or(
            ExpectedConditions.visibilityOfElementLocated(successText),
            ExpectedConditions.visibilityOfElementLocated(viewStatusBtn)
        ));
        System.out.println(">>> Success content found");

        // Клик по "Посмотреть статус"
        WebElement statusBtn = wait.until(ExpectedConditions.elementToBeClickable(viewStatusBtn));
        statusBtn.click();
        System.out.println(">>> 'Посмотреть статус' clicked");
    }

    // ==================== DEBUG: Проверка заполненности формы ====================

    private void debugFormValues() {
        try {
            System.out.println("\n>>> ===== FORM VALUES DEBUG =====");
            
            // Проверяем все инпуты
            List<WebElement> inputs = driver.findElements(By.tagName("input"));
            for (WebElement input : inputs) {
                String type = input.getAttribute("type");
                String placeholder = input.getAttribute("placeholder");
                String name = input.getAttribute("name");
                String value = input.getAttribute("value");
                boolean checked = Boolean.parseBoolean(input.getAttribute("checked"));
                
                if (type != null && type.equals("checkbox")) {
                    System.out.println(">>> Checkbox [" + placeholder + "]: " + (checked ? "CHECKED" : "unchecked"));
                } else if (!type.equals("hidden")) {
                    System.out.println(">>> Input [" + placeholder + "]: '" + (value != null ? value : "(empty)") + "'");
                }
            }
            
            // Проверяем выбранные dropdowns
            List<WebElement> selects = driver.findElements(By.cssSelector(".Dropdown-placeholder"));
            for (WebElement select : selects) {
                String text = select.getText();
                System.out.println(">>> Dropdown: '" + text + "'");
            }
            
            System.out.println(">>> ===== END FORM VALUES DEBUG =====\n");
        } catch (Exception e) {
            System.out.println(">>> Error debugging form values: " + e.getMessage());
        }
    }

    // ==================== ПРОВЕРКИ ====================

    public boolean isOrderSuccessful() {
        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(successText),
                    ExpectedConditions.visibilityOfElementLocated(viewStatusBtn)
            ));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getOrderNumber() {
        try {
            WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(orderNumber));
            return el.getText().replaceAll("[^0-9]", "");
        } catch (Exception e) {
            return null;
        }
    }

    public List<WebElement> getList() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(successElement));
        } catch (Exception e) {
            System.out.println("Warning: " + e.getMessage());
        }
        return driver.findElements(successElement);
    }

    // 🔹 Для отладки: вывести состояние модалки
    public void debugModal() {
        List<WebElement> modals = driver.findElements(modalDialog);
        System.out.println(">>> Modals on page: " + modals.size());

        if (!modals.isEmpty()) {
            List<WebElement> buttons = modals.get(0).findElements(By.tagName("button"));
            System.out.println(">>> Buttons in modal: " + buttons.size());
            for (WebElement btn : buttons) {
                System.out.println("  - '" + btn.getText() + "' | visible: " + btn.isDisplayed());
            }
        }
    }
}