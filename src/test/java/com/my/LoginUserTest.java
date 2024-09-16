package com.my;

import com.my.models.User;
import com.my.steps.CommonSteps;
import com.my.steps.UserSteps;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.my.constants.Messages.INCORRECT_PASS_OR_EMAIL_MSG;

public class LoginUserTest {

    private UserSteps userSteps;
    private CommonSteps commonSteps;
    private static User user;
    private String accessToken;

    @BeforeEach
    public void setUp() {
        userSteps = new UserSteps();
        commonSteps = new CommonSteps();
        user = GenerateUser.create();
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
    @DisplayName("Логин под существующим пользователем")
    public void existUserLoginTest() {
        Response response = userSteps.authUser(user.getEmail(), user.getPassword(), 200);
        commonSteps.checkSuccessAnswer(response, true);
    }

    @Test
    @DisplayName("логин с неверным логином")
    public void loginUserWithWrongLogin() {
        Response response = userSteps.authUser("pupaLupaMupa2@mail.ru", user.getPassword(), 401);
        commonSteps.checkMessageAnswer(response, INCORRECT_PASS_OR_EMAIL_MSG);
        commonSteps.checkSuccessAnswer(response, false);
    }

    @Test
    @DisplayName("логин с неверным паролем")
    public void loginUserWithWrongPassword() {
        Response response = userSteps.authUser(user.getEmail(), "hakunaMatata231", 401);
        commonSteps.checkMessageAnswer(response, INCORRECT_PASS_OR_EMAIL_MSG);
        commonSteps.checkSuccessAnswer(response, false);
    }
}
