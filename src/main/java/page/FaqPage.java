package page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;

import static org.testng.AssertJUnit.*;

public class FaqPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Локаторы элементов
    private static final By QUESTIONS_BLOCK = By.cssSelector(".accordion__button");
    private static final By ANSWER_PANEL = By.cssSelector(".accordion__panel p");
    private static final String QUESTION_XPATH_TEMPLATE = "//button[text()='%s']";
    private static final String ANSWER_XPATH_TEMPLATE = "//button[text()='%s']/following-sibling::div/p";

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
        questionElement.click();
    }

    /**
     * Находит элемент вопроса по тексту и ждёт его кликабельности.
     *
     * @param questionText текст вопроса
     * @return элемент вопроса (WebElement)
     */
    private WebElement findQuestionElement(String questionText) {
        By questionByXpath = By.xpath(String.format(QUESTION_XPATH_TEMPLATE, questionText));
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
        clickQuestion(questionText);
        By answerByXpath = By.xpath(String.format(ANSWER_XPATH_TEMPLATE, questionText));
        WebElement answerElement = wait.until(ExpectedConditions.visibilityOfElementLocated(answerByXpath));
        return answerElement.getText().trim(); // trim() для очистки от лишних пробелов
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
            WebElement questionElement = findQuestionElement(questionText);
            return questionElement.isDisplayed();
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
                .toList();
    }

    @Test
    public void testAnswersPanelLoading() {
        openPage();
        FaqPage faqPage = null;
        List<WebElement> answersPanels;
        answersPanels = Collections.unmodifiableList(faqPage.waitForAnswersPanel());
        assertNotNull("Панели с ответами не загружены", answersPanels);
        assertFalse("Не найдено ни одной панели с ответами", answersPanels.isEmpty());
    }

    private void openPage() {

    }
}