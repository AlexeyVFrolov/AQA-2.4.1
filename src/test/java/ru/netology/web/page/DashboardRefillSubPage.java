package ru.netology.web.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardRefillSubPage {

    private SelenideElement refillHeading = $("h1.heading");
    private SelenideElement amountInput = $("[data-test-id='amount'] input");
    private SelenideElement sourceCardNumberInput = $("[data-test-id='from'] input");
    private SelenideElement transferButton = $$("button").find(exactText("Пополнить"));
    private SelenideElement errorMessage = $("[data-test-id='action-transfer']");

    public DashboardRefillSubPage() {
        refillHeading.shouldBe(visible);
    }

    public DashboardCardListSubPage refillCard(long transferAmount, String cardNumber) {

        amountInput.sendKeys(Long.toString(transferAmount));
        sourceCardNumberInput.sendKeys(cardNumber);
        transferButton.click();
        return new DashboardCardListSubPage();

    }

    public boolean isError() {

        return errorMessage.shouldBe(visible).exists();

    }
}
