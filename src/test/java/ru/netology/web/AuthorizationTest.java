package ru.netology.web;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static ru.netology.utils.DataGenerator.Registration.*;

public class AuthorizationTest {

    private SelenideElement login;
    private SelenideElement password;
    private SelenideElement button;
    private SelenideElement error;

    @BeforeEach
    void setUp() {
        open("http://localhost:9999/");

        login = $("[data-test-id=login] .input__control");
        password = $("[data-test-id=password] .input__control");
        button = $("[data-test-id=action-login]");
        error = $("[data-test-id='error-notification'] .notification__content");
    }


    @Test
    public void shouldLoginWithActiveUser() {
        registerActiveUser();
        login.val(activeUser().getLogin());
        password.val(activeUser().getPassword());
        button.click();

        $$("h2").find(Condition.text("Личный кабинет")).shouldBe(visible);
    }


    @Test
    public void shouldNotLoginWithBlockedUser() {
        registerBlockedUser();
        login.val(blockedUser().getLogin());
        password.val(blockedUser().getPassword());
        button.click();

       error.shouldBe(visible).shouldHave(text("Ошибка! Пользователь заблокирован"));
    }


    @Test
    public void shouldNotLoginWithWrongLogin() {
        registerActiveUser();
        login.val(generateWrongLogin());
        password.val(activeUser().getPassword());
        button.click();

        error.shouldBe(visible).shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }


    @Test
    public void shouldNotLoginWithWrongPassword() {
        registerActiveUser();
        login.val(activeUser().getLogin());
        password.val(generateWrongPassword());
        button.click();

        error.shouldBe(visible).shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }
}
