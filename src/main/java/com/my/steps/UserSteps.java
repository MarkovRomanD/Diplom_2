package com.my.steps;

import com.my.models.User;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.HashMap;

import static com.my.constants.Urls.*;
import static io.restassured.RestAssured.given;

public class UserSteps extends RequestSpec {

    @Step("Создание пользователя")
    public Response createUser(User user, int expStatusCode) {
        return given()
                .spec(getSpec())
                .body(user)
                .post(REG_USER_URL)
                .then()
                .assertThat()
                .statusCode(expStatusCode)
                .extract()
                .response();
    }

    @Step("Удаление пользователя")
    public void deleteUser(String accessToken) {
        given()
                .header("authorization", accessToken)
                .spec(getSpec())
                .when()
                .delete(AUTH_USER_URL);
    }

    @Step("Получение токена авторизации")
    public String getAccessToken(Response response) {
        return response
                .then()
                .extract()
                .path("accessToken");
    }

    @Step("Авторизация пользователя")
    public Response authUser(String email, String password, int expStatusCode) {
        HashMap<String, String> creds = new HashMap<>();
        creds.put("email", email);
        creds.put("password", password);
        return given()
                .spec(getSpec())
                .body(creds)
                .when()
                .post(LOGIN_USER_URL)
                .then()
                .assertThat()
                .statusCode(expStatusCode)
                .extract()
                .response();
    }

    @Step("Изменение данных  неавторизованного пользователя")
    public Response patchUserWithoutAuth(User user, int expStatusCode) {
        return given()
                .spec(getSpec())
                .body(user)
                .patch(AUTH_USER_URL)
                .then()
                .assertThat()
                .statusCode(expStatusCode)
                .extract()
                .response();
    }

    @Step("Изменение данных  неавторизованного пользователя")
    public Response patchUserWithAuth(String accessToken, User user, int expStatusCode) {
        return given()
                .header("authorization", accessToken)
                .spec(getSpec())
                .body(user)
                .patch(AUTH_USER_URL)
                .then()
                .assertThat()
                .statusCode(expStatusCode)
                .extract()
                .response();
    }

}
