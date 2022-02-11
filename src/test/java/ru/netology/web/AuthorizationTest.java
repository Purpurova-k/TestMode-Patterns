package ru.netology.web;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.entities.RegistrationInfo;
import ru.netology.utils.DataGenerator;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class AuthorizationTest {

    private SelenideElement login;
    private SelenideElement password;
    private SelenideElement button;
    private SelenideElement error;

    @BeforeEach
    void setUp() {
        open("http://localhost:9999/");

        login = $("[data-test-id=login] input");
        password = $("[data-test-id=password] .input");
        button = $("[data-test-id=action-login]");
        error = $("[data-test-id='error-notification'] .notification__content");
    }


    @Test
    public void shouldLoginWithActiveUser() {
        RegistrationInfo registeredUser = DataGenerator.Registration.generateRegisteredUser("active");
        login.val(registeredUser.getLogin());
        password.val(registeredUser.getPassword());
        button.click();

        $$("h2").find(Condition.text("Личный кабинет")).shouldBe(visible);
    }


    @Test
    public void shouldNotLoginWithBlockedUser() {
        RegistrationInfo registeredUser = DataGenerator.Registration.generateRegisteredUser("blocked");
        login.val(registeredUser.getLogin());
        password.val(registeredUser.getPassword());
        button.click();

        error.shouldBe(visible).shouldHave(text("Ошибка! Пользователь заблокирован"));
    }


    @Test
    public void shouldNotLoginWithWrongLogin() {
        RegistrationInfo registeredUser = DataGenerator.Registration.generateRegisteredUser("active");
        login.val(DataGenerator.generateRandomLogin());
        password.val(registeredUser.getPassword());
        button.click();

        error.shouldBe(visible).shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }


    @Test
    public void shouldNotLoginWithWrongPassword() {
        RegistrationInfo registeredUser = DataGenerator.Registration.generateRegisteredUser("active");
        login.val(registeredUser.getLogin());
        password.val(DataGenerator.generateRandomPassword());
        button.click();

        error.shouldBe(visible).shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }
}
