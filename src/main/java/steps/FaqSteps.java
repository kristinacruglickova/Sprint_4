package steps;

import page.FaqPage;

public class FaqSteps {
    private final FaqPage faqPage;

    public FaqSteps(FaqPage faqPage) {
        this.faqPage = faqPage;
    }

    public void clickQuestion(String questionText) {
        faqPage.clickQuestion(questionText);
    }

    public String getAnswer(String questionText) {
        return faqPage.getAnswer(questionText);
    }
}
