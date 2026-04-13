import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import page.MainPage;
import steps.LogoSakamotoSteps;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class LogoSakamotoTest extends BaseTest {
    public LogoSakamotoTest(String browser) {
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
        LogoSakamotoSteps logoSamokatSteps = new LogoSakamotoSteps(mainPage);

        openPage();
        assertEquals("Переход на главную страницу сайта не выполнен", MainPage.URL, logoSamokatSteps.checkLogoSakamoto());
    }
}
