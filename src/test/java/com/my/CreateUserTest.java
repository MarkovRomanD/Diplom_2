package com.my;

import com.my.models.User;
import com.my.steps.CommonSteps;
import com.my.steps.UserSteps;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.my.constants.Messages.EMPTY_FIELD_REGISTER_USER_MSG;
import static com.my.constants.Messages.EXIST_USER_MSG;


public class CreateUserTest {
    private UserSteps userSteps;
    private CommonSteps commonSteps;
    private User user;
    private String accessToken;

    static Stream<Arguments> paramCreateUser() {
        return Stream.of(
                Arguments.of(GenerateUser.createEmail(), GenerateUser.createPassword(), null),
                Arguments.of(GenerateUser.createEmail(), null, GenerateUser.createName()),
                Arguments.of(null, GenerateUser.createPassword(), GenerateUser.createName())
        );
    }

    @BeforeEach
    public void setUp() {
        userSteps = new UserSteps();
        commonSteps = new CommonSteps();
        user = GenerateUser.create();
    }

    @AfterEach
    public void deleteUser(){
        if(accessToken != null){
            userSteps.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("создать уникального пользователя")
    public void createCorrectUserTest() {
        Response response = userSteps.createUser(user, 200);
        commonSteps.checkSuccessAnswer(response, true);
        accessToken = userSteps.getAccessToken(response);
    }

    @Test
    @DisplayName("создать пользователя, который уже зарегистрирован")
    public void createExistUserTest() {
        accessToken = userSteps.getAccessToken(userSteps.createUser(user, 200));
        Response response = userSteps.createUser(user, 403);
        commonSteps.checkSuccessAnswer(response, false);
        commonSteps.checkMessageAnswer(response, EXIST_USER_MSG);
    }

    @ParameterizedTest
    @DisplayName("создать пользователя и не заполнить одно из обязательных полей")
    @MethodSource("paramCreateUser")
    public void createUserWithoutOneOfRequiredFieldTest(String email, String password, String name) {
        User userParam = new User(email, password, name);
        Response response = userSteps.createUser(userParam, 403);
        commonSteps.checkSuccessAnswer(response, false);
        commonSteps.checkMessageAnswer(response, EMPTY_FIELD_REGISTER_USER_MSG);
    }

}
