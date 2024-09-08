package com.my.steps;

import com.my.models.Order;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import static com.my.constants.Urls.INGREDIENTS_URL;
import static com.my.constants.Urls.ORDERS_URL;
import static io.restassured.RestAssured.given;

public class OrderSteps extends RequestSpec {

    @Step("Создание заказа c авторизацией")
    public Response createOrder(String accessToken, Order order, int expStatusCode) {
        return given()
                .header("authorization", accessToken)
                .spec(getSpec())
                .body(order)
                .post(ORDERS_URL)
                .then()
                .assertThat()
                .statusCode(expStatusCode)
                .extract()
                .response();
    }

    @Step("Создание заказа без авторизации")
    public Response createOrderNoAuth(Order order, int expStatusCode) {
        return given()
                .spec(getSpec())
                .body(order)
                .post(ORDERS_URL)
                .then()
                .assertThat()
                .statusCode(expStatusCode)
                .extract()
                .response();
    }

    @Step("Получить рандомный список ингридиентов")
    public List<String> getRandomIngredients() {
        Response response = given()
                .spec(getSpec())
                .get(INGREDIENTS_URL)
                .then()
                .extract()
                .response();

        List<String> randomIngredients = response
                .body()
                .path("data._id");

        Collections.shuffle(randomIngredients);
        Random r = new Random();

        return randomIngredients.subList(1, r.nextInt((randomIngredients.size() - 1) + 1));
    }

    @Step("Получить заказы конкретного авторизованного пользователя")
    public Response getOrdersAuthUser(String accessToken, int expStatusCode) {

        return given()
                .header("authorization", accessToken)
                .spec(getSpec())
                .get(ORDERS_URL)
                .then()
                .assertThat()
                .statusCode(expStatusCode)
                .extract()
                .response();
    }

    @Step("Получить заказы конкретного авторизованного пользователя")
    public Response getOrdersNoAuthUser(int expStatusCode) {

        return given()
                .spec(getSpec())
                .get(ORDERS_URL)
                .then()
                .assertThat()
                .statusCode(expStatusCode)
                .extract()
                .response();
    }

}
