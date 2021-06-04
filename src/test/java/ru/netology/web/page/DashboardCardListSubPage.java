package ru.netology.web.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static ru.netology.web.data.DataHelper.getUserCardBalance;

public class DashboardCardListSubPage {

    private SelenideElement cardListHeading = $("h1.heading");
    private ElementsCollection cards = $$(".list__item");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";
    private SelenideElement reloadButton = $("[data-test-id='action-reload']");

    public DashboardCardListSubPage() {
        cardListHeading.shouldBe(visible);
    }

    public void getAllCardBalances(DataHelper.UserInfo user) {

        for (int i = 0; i < cards.size(); i++) {
            DataHelper.setUserCardBalance(user, i, getCardBalance(i));
        }

    }

    public long getCardBalance(int index) {

        val text = cards.get(index).text();

        return extractBalance(text);

    }

    private long extractBalance(String text) {

        val start = text.indexOf(balanceStart);
        val finish = text.indexOf(balanceFinish);
        val value = text.substring(start + balanceStart.length(), finish);

        return Long.parseLong(value);

    }

    public DashboardRefillSubPage chooseCardToRefill(String cardNumber) {

        val shortCardNumber = cardNumber.substring(cardNumber.length() - 4);

        cards.find(text(shortCardNumber)).find("button").click();
        return new DashboardRefillSubPage();

    }

    public long getTransferredAmount(DataHelper.UserInfo user, String cardNumber) {

        long newBalance;
        long oldBalance;

        String shortCardNumber = cardNumber.substring(cardNumber.length() - 4);
        String textExtractFrom = cards.find(text(shortCardNumber)).text();
        newBalance = extractBalance(textExtractFrom);
        oldBalance = getUserCardBalance(user, cardNumber);
        return newBalance - oldBalance;

    }

}
