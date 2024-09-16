package com.my.steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;

public class CommonSteps extends RequestSpec {


    @Step("Проверка на успешность {expectedSuccess} в теле ответа")
    public void checkSuccessAnswer(Response response, Boolean expectedSuccess) {
        response
                .then()
                .body("success", is(expectedSuccess));
    }

    @Step("Проверка сообщения {expectedMessage} в теле ответа")
    public void checkMessageAnswer(Response response, String expectedMessage) {
        response
                .then()
                .body("message", equalTo(expectedMessage));
    }
}
