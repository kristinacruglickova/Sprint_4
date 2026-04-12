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
     * 🔹 Надёжный клик по кнопке "Заказать"
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

        // 1. Ждём появления модалки
        wait.until(ExpectedConditions.visibilityOfElementLocated(modalDialog));
        System.out.println(">>> Modal found");

        // 2. Ждём исчезновения оверлея внутри модалки
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(overlay));
        } catch (Exception ignored) {}

        // 3. Ищем кнопку "Да"
        WebElement yesBtn;
        try {
            yesBtn = wait.until(ExpectedConditions.presenceOfElementLocated(confirmYesBtn));
            System.out.println(">>> 'Да' button found");
        } catch (Exception e) {
            System.out.println(">>> Primary locator failed, trying backup...");
            yesBtn = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//div[contains(@class,'Order_Buttons')]//button[normalize-space()='Да']")
            ));
            System.out.println(">>> 'Да' button found via backup locator");
        }

        // Убеждаемся, что кнопка видима
        wait.until(ExpectedConditions.visibilityOf(yesBtn));
        System.out.println(">>> 'Да' button is visible");

        // 🔍 DEBUG: Проверяем, что это именно кнопка "Да" (последняя)
        List<WebElement> allButtons = driver.findElements(By.xpath("//div[contains(@class,'Order_Modal')]//button"));
        System.out.println(">>> Found " + allButtons.size() + " buttons in modal");
        for (int i = 0; i < allButtons.size(); i++) {
            System.out.println(">>> Button " + i + ": " + allButtons.get(i).getText() + " (displayed: " + allButtons.get(i).isDisplayed() + ")");
        }
        
        // Берём именно последнюю кнопку (это "Да")
        WebElement yesBtnLast = allButtons.get(allButtons.size() - 1);
        System.out.println(">>> Target button text: '" + yesBtnLast.getText() + "'");

        // 4. Кликаем - несколько попыток разными способами
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", yesBtnLast);
        
        try {
            Thread.sleep(200);
        } catch (InterruptedException ignored) {}

        // Инициализируем Actions для всех попыток
        Actions actions = new Actions(driver);
        
        // Попытка 0: Используем Tab + Enter (клавиатурная навигация)
        boolean clicked = false;
        try {
            System.out.println(">>> Attempting keyboard navigation (Tab + Enter)...");
            // Используем Actions для отправки клавиш
            actions.click(yesBtnLast).perform();
            Thread.sleep(50);
            actions.sendKeys(Keys.ENTER).perform();
            System.out.println(">>> 'Да' activated via Actions keyboard (Enter)");
            clicked = true;
        } catch (Exception e0) {
            System.out.println(">>> Keyboard navigation failed: " + e0.getMessage());
        }

        // Попытка 0b: Проверяем наличие невидимого оверлея
        if (!clicked) {
            try {
                System.out.println(">>> Checking for overlays...");
                List<WebElement> overlays = driver.findElements(By.xpath("//*[contains(@class,'Overlay')]"));
                System.out.println(">>> Found " + overlays.size() + " overlay elements");
            } catch (Exception eCheck) {
                System.out.println(">>> No overlays detected");
            }
        }

        // Попытка 0c: Проверяем если может быть ошибка в консоли браузера
        if (!clicked) {
            try {
                System.out.println(">>> Checking for button onClick attributes...");
                String onClickAttr = yesBtnLast.getAttribute("onclick");
                String dataAttr = yesBtnLast.getAttribute("data-test");
                System.out.println(">>> Button onclick attr: " + onClickAttr);
                System.out.println(">>> Button data-test attr: " + dataAttr);
                
                // Пробуем кликнуть на родителя кнопки
                System.out.println(">>> Trying to click button's parent element...");
                WebElement btnParent = (WebElement) ((JavascriptExecutor) driver).executeScript(
                    "return arguments[0].parentElement;", yesBtnLast
                );
                btnParent.click();
                System.out.println(">>> Parent element clicked");
                clicked = true;
            } catch (Exception eParent) {
                System.out.println(">>> Parent click failed: " + eParent.getMessage());
            }
        }

        // Попытка 1: Кликнуть на родительский контейнер (может быть, React слушает там)
        if (!clicked) {
            try {
                WebElement buttonsContainer = driver.findElement(By.xpath("//div[contains(@class,'Order_Modal')]//div[contains(@class,'Order_Buttons')]"));
                // Находим индекс нужной кнопки в контейнере
                List<WebElement> buttonsInContainer = buttonsContainer.findElements(By.tagName("button"));
                WebElement targetBtn = buttonsInContainer.get(buttonsInContainer.size() - 1); // последняя кнопка
                
                // Кликаем на неё несколько раз подряд
                ((JavascriptExecutor) driver).executeScript("arguments[0].focus();", targetBtn);
                Thread.sleep(100);
                targetBtn.click();
                System.out.println(">>> 'Да' clicked via Selenium click #1");
                Thread.sleep(100);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", targetBtn);
                System.out.println(">>> 'Да' clicked via JS click #2");
                clicked = true;
            } catch (Exception e1) {
                System.out.println(">>> Initial click attempt failed: " + e1.getMessage());
            }
        }

        // Попытка 2: JavaScript с более полным event triggering
        if (!clicked) {
            try {
                System.out.println(">>> Trying rapid multiple clicks + complex event sequence...");
                ((JavascriptExecutor) driver).executeScript(
                    "var btn = arguments[0]; " +
                    "btn.focus(); " +
                    "for (let i = 0; i < 3; i++) { " +
                    "  btn.click(); " +
                    "  var evt = new PointerEvent('click', {bubbles: true, cancelable: true, view: window}); " +
                    "  btn.dispatchEvent(evt); " +
                    "} " +
                    "var evt3 = new CustomEvent('confirm', {bubbles: true, detail: 'yes'}); " +
                    "btn.dispatchEvent(evt3); ",
                    yesBtnLast
                );
                System.out.println(">>> 'Да' clicked via rapid multi-click with PointerEvent");
                clicked = true;
            } catch (Exception e2) {
                System.out.println(">>> Rapid multi-click failed: " + e2.getMessage());
            }
        }

        // Попытка 3: Actions с методом doubleClick
        if (!clicked) {
            try {
                actions.moveToElement(yesBtnLast).click().click().perform();
                System.out.println(">>> 'Да' clicked via double Actions click");
                clicked = true;
            } catch (Exception e3) {
                System.out.println(">>> Double Actions click failed: " + e3.getMessage());
            }
        }

        if (!clicked) {
            System.out.println(">>> WARNING: 'Да' button click might not have worked!");
        }

        // 5. Даём время на обновление контента
        try {
            Thread.sleep(1000);  // Увеличена пауза с 500 до 1000 мс
        } catch (InterruptedException ignored) {}

        // 🔍 DEBUG: Выводим HTML модалки после клика
        try {
            WebElement modal = driver.findElement(modalDialog);
            String modalHTML = modal.getAttribute("innerHTML");
            System.out.println(">>> Modal HTML after click:\n" + modalHTML.substring(0, Math.min(500, modalHTML.length())));
        } catch (Exception e) {
            System.out.println(">>> Could not get modal HTML: " + e.getMessage());
        }

        // 6. 🔹 Ждём, что модалка обновила контент (появился текст успеха)
        System.out.println(">>> Waiting for success content...");
        
        // Создаём долгое ожидание (20 секунд для успеха)
        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(20));
        
        // Сначала пробуем найти по узким условиям
        boolean found = false;
        try {
            longWait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(successText),
                    ExpectedConditions.visibilityOfElementLocated(viewStatusBtn)
            ));
            System.out.println(">>> Success content found");
            found = true;
        } catch (Exception e) {
            System.out.println(">>> Narrow search failed after 20s, trying broader search...");
            // Если не нашли в модалке, ищем везде
            try {
                longWait.until(ExpectedConditions.or(
                        ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(., 'Заказ оформлен')]")),
                        ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(., 'Посмотреть статус')]")),
                        ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(normalize-space(.), 'Посмотреть статус')]"))
                ));
                System.out.println(">>> Success content found on page");
                found = true;
            } catch (Exception e2) {
                System.out.println(">>> Success content not found anywhere after 40s total");
                
                // DEBUG: показываем что осталось в модалке
                try {
                    WebElement modal = driver.findElement(modalDialog);
                    String modalHTML = modal.getAttribute("innerHTML");
                    System.out.println(">>> Final modal HTML:\n" + (modalHTML.length() > 1000 ? modalHTML.substring(0, 1000) : modalHTML));
                } catch (Exception ex) {
                    System.out.println(">>> Could not get modal HTML: " + ex.getMessage());
                }
                
                throw e2;
            }
        }

        // 7. Кликаем на "Посмотреть статус" для редиректа
        WebElement statusBtn;
        try {
            statusBtn = wait.until(ExpectedConditions.elementToBeClickable(viewStatusBtn));
        } catch (Exception e) {
            // Пробуем найти кнопку не в модалке
            statusBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(., 'Посмотреть статус')]")));
        }
        
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", statusBtn);
        try {
            actions.moveToElement(statusBtn).click().perform();
            System.out.println(">>> 'Посмотреть статус' clicked via Actions");
        } catch (Exception e) {
            try {
                statusBtn.click();
                System.out.println(">>> 'Посмотреть статус' clicked via Selenium");
            } catch (Exception e2) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", statusBtn);
                System.out.println(">>> 'Посмотреть статус' clicked via JS");
            }
        }
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