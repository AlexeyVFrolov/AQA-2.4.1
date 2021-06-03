package ru.netology.web.test;

import lombok.val;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.netology.web.data.DataHelper;

import ru.netology.web.page.LoginPage;
import ru.netology.web.page.VerificationPage;
import ru.netology.web.page.DashboardCardListSubPage;
import ru.netology.web.page.DashboardRefillSubPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.netology.web.data.DataHelper.*;

class MoneyTransferTest {
    private DataHelper.UserInfo userInfo;
    private LoginPage loginPage;
    private VerificationPage verificationPage;
    private DashboardCardListSubPage dashboardCardListSubPage;
    private DashboardRefillSubPage dashboardRefillSubPage;

    @BeforeEach
    void shouldLoginUser() {

        open("http://localhost:9999");
        val loginPage = new LoginPage();
        userInfo = DataHelper.getUserInfo(); // берем данные пользователя
        verificationPage = loginPage.validLogin(userInfo); // вводим логин и пароль на странице входа, переходим на страницу верификации
        val verificationCode = DataHelper.getVerificationCodeFor(userInfo); // берем подготовленный код верификации
        dashboardCardListSubPage = verificationPage.validVerify(verificationCode); // вводим код проверки на странице верификации, переходим на страницу личного кабинета, подстраницу списка карт

    }

    @Test
    void shouldTransferMoneyWithinBalance() {

        // считываем текущие значения баланса каждой карты из списка карт
        dashboardCardListSubPage.getAllCardBalances(userInfo);
        // берем подготовленный номер карты-получателя залогиненного пользователя
        val recipientCardNumber = getCardNumber(userInfo, 0);
        // выбираем карту для пополнения на подстранице списка карт, переходим на подстраницу пополнения карты
        dashboardRefillSubPage = dashboardCardListSubPage.chooseCardToRefill(recipientCardNumber);
        // берем номер карты-источника залогиненного пользователя и подготовленную сумму пополнения в пределах баланса карты-источника
        val sourceCardNumber = getCardNumber(userInfo, 1);
        val transferAmount = getValidTransferAmount(userInfo, sourceCardNumber);
        // вводим сумму и карту-источник, возвращаемся на подстраницу списка карт
        dashboardCardListSubPage = dashboardRefillSubPage.refillCard(transferAmount, sourceCardNumber);
        // проверяем правильно ли прошел перевод
        assertEquals(transferAmount, dashboardCardListSubPage.getTransferredAmount(userInfo, recipientCardNumber));
        assertEquals(-transferAmount, dashboardCardListSubPage.getTransferredAmount(userInfo, sourceCardNumber));

    }

    @Test
    void shouldNotTransferMoneyFromWrongCardNumber() {

        // считываем текущие значения баланса каждой карты из списка карт
        dashboardCardListSubPage.getAllCardBalances(userInfo);
        // берем подготовленный номер карты-получателя залогиненного пользователя
        val recipientCardNumber = getCardNumber(userInfo, 0);
        // выбираем карту для пополнения на подстранице списка карт, переходим на подстраницу пополнения карты
        dashboardRefillSubPage = dashboardCardListSubPage.chooseCardToRefill(recipientCardNumber);
        // берем номер карты-источника залогиненного пользователя и подготовленную сумму пополнения больше доступной на  карте-источнике
        val sourceCardNumber = getWrongCardNumber(userInfo);
        val transferAmount = getInValidTransferAmount(userInfo, sourceCardNumber);
        // вводим сумму и карту-источник, возвращаемся на подстраницу списка карт
        dashboardCardListSubPage = dashboardRefillSubPage.refillCard(transferAmount, sourceCardNumber);
        // проверяем правильно ли прошел перевод / сообщение об ошибке
        assertTrue(dashboardRefillSubPage.isError());

    }

    @Test
    void shouldNotTransferMoneyIfOutOfBalance() {

        // считываем текущие значения баланса каждой карты из списка карт
        dashboardCardListSubPage.getAllCardBalances(userInfo);
        // берем подготовленный номер карты-получателя залогиненного пользователя
        val recipientCardNumber = getCardNumber(userInfo, 0);
        // выбираем карту для пополнения на подстранице списка карт, переходим на подстраницу пополнения карты
        dashboardRefillSubPage = dashboardCardListSubPage.chooseCardToRefill(recipientCardNumber);
        // берем номер карты-источника залогиненного пользователя и подготовленную сумму пополнения больше доступной на  карте-источнике
        val sourceCardNumber = getCardNumber(userInfo, 1);
        val transferAmount = getInValidTransferAmount(userInfo, sourceCardNumber);
        // вводим сумму и карту-источник, возвращаемся на подстраницу списка карт
        dashboardCardListSubPage = dashboardRefillSubPage.refillCard(transferAmount, sourceCardNumber);
        // проверяем правильно ли прошел перевод / сообщение об ошибке
        assertTrue(dashboardRefillSubPage.isError());

    }

}
