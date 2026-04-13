package steps;

import page.MainPage;

public class LogoYandexSteps {

    private final MainPage mainPage;

    public LogoYandexSteps(MainPage mainPage) {
        this.mainPage = mainPage;
    }

    public void checkLogoYandex() {
        mainPage.clickLogoYandex();
    }

    public String getUrlOpenedInSecondTab() {
        return mainPage.getUrlOpenedInSecondTab();
    }
}
