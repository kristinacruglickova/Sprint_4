import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import steps.LogoYandexSteps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class LogoYandexTest extends BaseTest {
    public LogoYandexTest(String browser) {
        super(browser);
    }

    @Parameterized.Parameters(name = "{0}")
    public static Object[][] getBrowserSelection() {
        return new Object[][]{
                {"chrome"},
                {"firefox"}
        };
    }
    @Test
    public void successfulClickLogo() {
        LogoYandexSteps logoYandexSteps = new LogoYandexSteps(mainPage);

        openPage();
        logoYandexSteps.checkLogoYandex();

        String actualUrl = logoYandexSteps.getUrlOpenedInSecondTab();
        assertTrue(
                "Переход на главную страницу Яндекса не выполнен",
                actualUrl.contains("yandex.ru") || actualUrl.contains("dzen.ru"));
    }
}
