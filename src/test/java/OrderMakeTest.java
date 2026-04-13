import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import steps.OrderMakeSteps;

import static org.junit.Assert.assertFalse;

@RunWith(Parameterized.class)
public class OrderMakeTest extends BaseTest {

    public String name;
    public String lastName;
    public String address;
    public int station;
    public String phone;
    public String date;
    public int period;
    public String option;

    public OrderMakeTest(String browser,
                         String name,
                         String lastName,
                         String address,
                         int station,
                         String phone,
                         String date,
                         int period,
                         String option) {
        super(browser);
        this.name = name;
        this.lastName = lastName;
        this.address = address;
        this.station = station;
        this.phone = phone;
        this.date = date;
        this.period = period;
        this.option = option;
    }

    @Parameterized.Parameters(name = "{0}: {1} {2} {3} {4} {5} {6} {7} {8}")
    public static Object[][] getBrowserSelection() {
        return new Object[][]{
                {"chrome", "Кристина", "Кругликова", "Пушкина 13", 3, "+7917445297", "16.04.2026", 3, "серая безысходность"},
                {"chrome", "Анатолий", "Петров", "Николаева 4", 5, "+78008065488", "17.04.2026", 2, "чёрный жемчуг"},
                {"firefox", "Кристина", "Кругликова", "Пушкина 13", 3, "+79456971297", "18.04.2026", 3, "серая безысходность"},
                {"firefox", "Анатолий", "Петров", "Николаева 4", 5, "+78008529988", "19.04.2026", 2, "чёрный жемчуг"}
        };
    }

    @Test
    public void successfulOrderUp() {
        OrderMakeSteps orderSteps = new OrderMakeSteps(orderMakePage);
        openPage();

        mainPage.clickUpOrderButton();
        orderSteps.firstStepForWhom(name, lastName, address, station, phone);
        orderSteps.secondaryStepAboutRental(date, period, option);

        // 🔹 1. Нажать "Заказать"
        orderMakePage.clickOrderButton();

        // 🔹 2. Подтвердить в модалке (клик по "Да")
        orderMakePage.confirmAction();

        // 🔹 3. НОВОЕ: Кликнуть "Посмотреть статус" (если нужно проверить переход)
        // orderMakePage.clickViewStatusButton();  // ← раскомментируйте, если нужно кликнуть

        // 🔹 4. Проверка: заказ оформлен
        assertFalse("Переход на страницу подтверждения не выполнен",
                orderMakePage.getList().isEmpty());
    }

    @Test
    public void successfulOrderDown() {
        OrderMakeSteps orderSteps = new OrderMakeSteps(orderMakePage);
        openPage();

        mainPage.clickDownOrderButton();
        orderSteps.firstStepForWhom(name, lastName, address, station, phone);
        orderSteps.secondaryStepAboutRental(date, period, option);

        orderMakePage.clickOrderButton();
        orderMakePage.confirmAction();
        // orderMakePage.clickViewStatusButton();  // ← при необходимости

        assertFalse("Переход на страницу подтверждения не выполнен",
                orderMakePage.getList().isEmpty());
    }


}