package steps;

import page.OrderMakePage;

public class OrderMakeSteps {
    public OrderMakePage orderMakePage;

    public OrderMakeSteps(OrderMakePage orderMakePage) {
        this.orderMakePage = orderMakePage;
    }

    public void firstStepForWhom(String name, String lastName, String address, int station, String phone) {
        orderMakePage.enterFirstName(name);
        //ВВести в поле фамилию
        orderMakePage.enterLastName(lastName);
        // Ввести адрес
        orderMakePage.enterAddress(address);
        // Кликнуть по полю станции
        orderMakePage.clickMetroField();
        // Выбрать станцию
        orderMakePage.selectStation(station);
        // ввести номер
        orderMakePage.enterNumber(phone);
        // нажать далее для перехода на следующее окно
        orderMakePage.clickNextButton();
    }

    public void secondaryStepAboutRental(String date, int period, String option) {
        //выбрать дату
        orderMakePage.selectDate(date);
        //кликнуть на поле аренды
        orderMakePage.clickRentalField();
        // выбрать срок аренды
        orderMakePage.selectRentalPeriod(period);
        // выбрать параметр самоката
        orderMakePage.selectScooterOption(option);
        // нажать кнопку "Заказать"
        orderMakePage.clickOrderButton();
        // подтвердить
        orderMakePage.confirmAction();
    }
}
