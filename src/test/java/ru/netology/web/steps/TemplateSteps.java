package ru.netology.web.steps;

import com.codeborne.selenide.Selenide;
import io.cucumber.java.ru.И;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Пусть;
import io.cucumber.java.ru.Тогда;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;
import ru.netology.web.page.TransferPage;
import ru.netology.web.page.VerificationPage;

import static com.codeborne.selenide.Selenide.open;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


public class TemplateSteps {
    private static LoginPage loginPage;
    private static DashboardPage dashboardPage;
    private static VerificationPage verificationPage;
    private static TransferPage transferPage;
    private static DataHelper dataHelper;


    @Пусть("пользователь залогинен с именем {string}, паролем {string} и кодом {string}")
    public void loginWithNameAndPassword(String login, String password, String verificationCode) {
        loginPage = Selenide.open("http://localhost:9999", LoginPage.class);
        verificationPage = loginPage.validLogin(new DataHelper.AuthInfo(login, password));
        dashboardPage = verificationPage.validVerify(new DataHelper.VerificationCode(verificationCode));
        dashboardPage.verifyIsDashboardPage();
    }

    @Когда("пользователь переводит {string} рублей с карты с номером {string} на свою {string} карту с главной страницы")
    public void validTransfer(String amount, String cardNumber, String index) {
        int adjustedIndex = Integer.parseInt(index) - 1;
        transferPage = dashboardPage.selectCardToTransfer(adjustedIndex);
        dashboardPage = transferPage.makeValidTransfer(amount, cardNumber);
    }

    @Тогда("баланс его {string} карты из списка на главной странице должен стать {string} рублей")
    public void validateCardBalance(String index, String expectedBalance) {
        int cardIndex = Integer.parseInt(index) - 1;
        int expectedBalanceInt = Integer.parseInt(expectedBalance);
        int actualBalance = dashboardPage.getCardBalance(cardIndex);
        assertThat(actualBalance, equalTo(expectedBalanceInt));
    }
}