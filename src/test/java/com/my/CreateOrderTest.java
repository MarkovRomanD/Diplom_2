package com.my;

import com.my.models.Order;
import com.my.models.User;
import com.my.steps.CommonSteps;
import com.my.steps.OrderSteps;
import com.my.steps.UserSteps;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.my.constants.Messages.EMPTY_INGREDIENTS_ORDER_MSG;

public class CreateOrderTest {
    private CommonSteps commonSteps;
    private OrderSteps orderSteps;
    private UserSteps userSteps;

    private String accessToken;
    private Order order;

    @BeforeEach
    public void setUp() {
        commonSteps = new CommonSteps();
        orderSteps = new OrderSteps();
        userSteps = new UserSteps();
        User user = GenerateUser.create();
        Response response = userSteps.createUser(user, 200);
        accessToken = userSteps.getAccessToken(response);
    }

    @AfterEach
    public void deleteUser() {
        if (accessToken != null) {
            userSteps.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и ингридиентами")
    public void createOrderWithAuthIngredients() {
        order = new Order(orderSteps.getRandomIngredients());
        Response response = orderSteps.createOrder(accessToken, order, 200);
        commonSteps.checkSuccessAnswer(response, true);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией без ингридиентов")
    public void createOrderWithAuthNoIngredients() {
        order = new Order();
        Response response = orderSteps.createOrder(accessToken, order, 400);
        commonSteps.checkSuccessAnswer(response, false);
        commonSteps.checkMessageAnswer(response, EMPTY_INGREDIENTS_ORDER_MSG);
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void createOrderWithoutAuth() {
        order = new Order(orderSteps.getRandomIngredients());
        Response response = orderSteps.createOrderNoAuth(order, 200);
        commonSteps.checkSuccessAnswer(response, true);
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    public void createOrderInvalidHashIngredients() {
        List<String> itemList = new ArrayList<>();
        itemList.add("61c0c5a71ddd1f82001bdaaa6d2");
        order = new Order(itemList);
        orderSteps.createOrder(accessToken, order, 500);
    }
}
