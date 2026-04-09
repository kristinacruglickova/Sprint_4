package page;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class OrderMakePage {

    private final WebDriver driver;
    private final By confirmActionBtn = By.xpath("//button[text()='Да']");
    private final By rentalPeriod = By.className("Dropdown-option");
    private final By clkRentalField = By.className("Dropdown-placeholder");
    private final By dateField = By.xpath(".//input[@placeholder='* Когда привезти самокат']");
    private final By nextBtn = By.xpath("//button[text()=\"Далее\"]");
    private final By number = By.cssSelector("input[placeholder=\"* Телефон: на него позвонит курьер\"]");
    private final By metroField = By.xpath(".//input[@placeholder='* Станция метро']");

    private final By addressField = By.cssSelector("input[placeholder=\"* Адрес: куда привезти заказ\"]");
    private final By blackCheck = By.xpath("//label[text()='чёрный жемчуг']/input[@type='checkbox']");
    private final By greyCheck = By.xpath("//label[text()='серая безысходность']/input[@type='checkbox']");
    private final By lastNameField = By.cssSelector("input[placeholder=\"* Фамилия\"]");
    private final By firstNameField = By.cssSelector("input[placeholder=\"* Имя\"]");
    private final By orderBtn = By.xpath(".//button[contains(@class, 'Button_Middle__1CSJM') and text()='Заказать']");
    private final By controlElement = By.xpath("//div[text()='Заказ оформлен']");
    private final By allStation = By.xpath("//div[@class=\"select-search__select\"]//li");

    public OrderMakePage(WebDriver driver) {
        this.driver = driver;
    }

    // Ввести в поле имя
    public void enterFirstName(String firstName) {
        driver.findElement(firstNameField).sendKeys(firstName);
    }

    //ВВести в поле фамилию
    public void enterLastName(String lastName) {
        driver.findElement(lastNameField).sendKeys(lastName);
    }

    // Ввести адрес
    public void enterAddress(String address) {
        driver.findElement(addressField).sendKeys(address);
    }

    // Кликнуть по полю станции
    public void clickMetroField() {
        driver.findElement(metroField).click();
        ;
    }

    // Выбрать станцию
    public void selectStation(int metro) {
        List<WebElement> elements = driver.findElements(allStation);
        elements.get(metro - 1).click();  // Выбор станции метро по индексу
    }

    // ввести номер
    public void enterNumber(String phoneNumber) {
        driver.findElement(number).sendKeys(phoneNumber);
    }

    // нажать далее для перехода на следующее окно
    public void clickNextButton() {
        driver.findElement(nextBtn).click();
    }

    //выбрать дату
    public void selectDate(String date) {
        driver.findElement(dateField).sendKeys(date);
        driver.findElement(dateField).sendKeys(Keys.RETURN);
    }

    //кликнуть на поле аренды
    public void clickRentalField() {
        driver.findElement(clkRentalField).click();
    }

    // выбрать срок аренды
    public void selectRentalPeriod(int rentalDays) {
        List<WebElement> elements = driver.findElements(rentalPeriod);
        elements.get(rentalDays - 1).click();  // Выбор срока аренды
    }

    // выбрать параметр самоката
    public void selectScooterOption(String color) {
        if ("чёрный жемчуг".equals(color)) {
            driver.findElement(blackCheck).click();
        } else if ("серая безысходность".equals(color)) {
            driver.findElement(greyCheck).click();
        }
    }

    // нажать кнопку "Заказать"
    public void clickOrderButton() {
        driver.findElement(orderBtn).click();
    }

    // подтвердить
    public void confirmAction() {
        driver.findElement(confirmActionBtn).click();
    }

    //проверка перехода на модальное окно путем поиска специфического элемента
    public List<WebElement> getList() {
        return driver.findElements(controlElement);
    }
}
