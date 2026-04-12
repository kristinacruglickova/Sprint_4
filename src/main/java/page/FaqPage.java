package page;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.stream.Collectors;

public class FaqPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Локаторы элементов - ищем кнопки и панели аккордеона более гибко
    private static final By QUESTIONS_BLOCK = By.xpath("//button[contains(@class, 'accordion') or contains(@class, 'Accordion')]");
    private static final By ANSWER_PANEL = By.xpath("//div[contains(@class, 'accordion') or contains(@class, 'Accordion')]//*[self::p or self::span or self::div[@class]]");

    public FaqPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    /**
     * Кликает по вопросу (раскрывает блок с ответом).
     *
     * @param questionText текст вопроса для поиска
     */
    public void clickQuestion(String questionText) {
        WebElement questionElement = findQuestionElement(questionText);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", questionElement);
        wait.until(ExpectedConditions.elementToBeClickable(questionElement)).click();
    }

    /**
     * Находит элемент вопроса по тексту и ждёт его кликабельности.
     *
     * @param questionText текст вопроса
     * @return элемент вопроса (WebElement)
     */
    private WebElement findQuestionElement(String questionText) {
        By questionByXpath = By.xpath(String.format("//*[normalize-space(.) = '%s']", questionText));
        return wait.until(ExpectedConditions.elementToBeClickable(questionByXpath));
    }

    /**
     * Получает текст ответа на заданный вопрос.
     * Сначала кликает по вопросу, затем находит и возвращает текст ответа.
     *
     * @param questionText текст вопроса
     * @return текст ответа
     */
    public String getAnswer(String questionText) {
        WebElement questionElement = findQuestionElement(questionText);
        
        // Скролл до вопроса
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", questionElement);
        
        // Клик по вопросу
        wait.until(ExpectedConditions.elementToBeClickable(questionElement)).click();
        
        // После клика ищем все параграфы и возвращаем первый видимый с текстом (отличный от вопроса)
        wait.until(d -> {
            List<WebElement> paragraphs = d.findElements(By.xpath("//p[normalize-space() != '']"));
            return paragraphs.stream()
                    .filter(WebElement::isDisplayed)
                    .filter(p -> !p.getText().contains(questionText) && p.getText().length() > 5)
                    .findFirst()
                    .orElse(null) != null;
        });
        
        List<WebElement> paragraphs = driver.findElements(By.xpath("//p[normalize-space() != '']"));
        for (WebElement p : paragraphs) {
            if (p.isDisplayed() && !p.getText().contains(questionText) && p.getText().length() > 5) {
                return p.getText().trim();
            }
        }
        
        throw new org.openqa.selenium.TimeoutException("Не удалось найти ответ на вопрос: " + questionText);
    }

    /**
     * Дожидается загрузки всех панелей с ответами и возвращает их список.
     *
     * @return список элементов панелей с ответами
     */
    public List<WebElement> waitForAnswersPanel() {
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(ANSWER_PANEL));
    }

    /**
     * Проверяет, виден ли заданный вопрос на странице.
     *
     * @param questionText текст вопроса
     * @return true, если вопрос виден, false — иначе
     */
    public boolean isQuestionVisible(String questionText) {
        try {
            List<WebElement> questionElements = driver.findElements(By.xpath("//*[contains(normalize-space(), '" + questionText + "')]"));
            for (WebElement elem : questionElements) {
                if (elem.isDisplayed() && (elem.getTagName().equals("button") || elem.getAttribute("class").contains("accordion"))) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Получает список всех вопросов, отображённых на странице.
     *
     * @return список текстов вопросов
     */
    public List<String> getQuestionsTexts() {
        List<WebElement> questionElements = driver.findElements(QUESTIONS_BLOCK);
        return questionElements.stream()
                .map(WebElement::getText)
                .map(String::trim)
                .collect(Collectors.toList()); // Совместимо с Java 11
    }

    /**
     * Переход на страницу FAQ.
     *
     * @param url адрес страницы
     */
    public void openPage(String url) {
        driver.get(url);
    }
}