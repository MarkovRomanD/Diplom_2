package com.my;

import com.my.models.Order;
import com.my.models.User;
import com.my.steps.CommonSteps;
import com.my.steps.OrderSteps;
import com.my.steps.UserSteps;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static com.my.constants.Messages.SHOULD_AUTHORIZED_MSG;

public class GetOrdersFromUserTest {
    private CommonSteps commonSteps;
    private OrderSteps orderSteps;
    private UserSteps userSteps;

    private String accessToken;

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

    //по документации не понятно, total в конкретном запросе относится к пользователю или к заказам во всей системе
    @Test
    @DisplayName("Получение заказов от авторизованного пользователя")
    public void getOrderFromAuthUser() {
        orderSteps.createOrder(accessToken, new Order(orderSteps.getRandomIngredients()), 200);
        Response response = orderSteps.getOrdersAuthUser(accessToken, 200);
        commonSteps.checkSuccessAnswer(response, true);
        Assertions.assertEquals(1, (Integer) response.body().path("total"));
    }

    @Test
    @DisplayName("Получение заказов от неавторизованного пользователя")
    public void getOrderFromNoAuthUser() {
        Response response = orderSteps.getOrdersNoAuthUser(401);
        commonSteps.checkSuccessAnswer(response, false);
        commonSteps.checkMessageAnswer(response, SHOULD_AUTHORIZED_MSG);
    }
}
