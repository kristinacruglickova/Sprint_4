package steps;

import page.MainPage;

public class LogoSakamotoSteps {

    private final MainPage mainPage;

    public LogoSakamotoSteps(MainPage mainPage) {
        this.mainPage = mainPage;
    }

    public String checkLogoSakamoto() {
        mainPage.clickUpOrderButton();
        mainPage.clickLogoSakamoto();
        return mainPage.getCurrentUrl();
    }
}


