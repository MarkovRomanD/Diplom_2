package com.my;

import com.my.models.User;
import com.my.steps.CommonSteps;
import com.my.steps.UserSteps;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.my.constants.Messages.SHOULD_AUTHORIZED_MSG;

public class ChangeDataUserTest {
    private UserSteps userSteps;
    private CommonSteps commonSteps;
    private User user;
    private String accessToken;

    static Stream<Arguments> paramPatchUser() {
        return Stream.of(
                Arguments.of("umpa", "lumpa", "pumpa"),
                Arguments.of("umpa", "lumpa", ""),
                Arguments.of("umpa", "", "pumpa"),
                Arguments.of("", "lumpa", "pumpa"),
                Arguments.of("umpa", "", ""),
                Arguments.of("", "lumpa", ""),
                Arguments.of("", "", "pumpa"),
                Arguments.of("", "", "")
        );
    }

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

    @ParameterizedTest
    @MethodSource("paramPatchUser")
    @DisplayName("Изменение данных пользователя без авторизации")
    public void updateDataUserWithoutAuthTest(String changeEmail, String changePass, String changeName) {
        user.setEmail(user.getEmail() + changeEmail);
        user.setPassword(user.getPassword() + changePass);
        user.setName(user.getName() + changeName);
        Response response = userSteps.patchUserWithoutAuth(user, 401);
        commonSteps.checkSuccessAnswer(response, false);
        commonSteps.checkMessageAnswer(response, SHOULD_AUTHORIZED_MSG);
    }

    @ParameterizedTest
    @MethodSource("paramPatchUser")
    @DisplayName("Изменение данных пользователя с авторизацией")
    public void updateDataUserWithAuthTest(String changeEmail, String changePass, String changeName) {
        user.setEmail(user.getEmail() + changeEmail);
        user.setPassword(user.getPassword() + changePass);
        user.setName(user.getName() + changeName);
        Response response = userSteps.patchUserWithAuth(accessToken, user, 200);
        commonSteps.checkSuccessAnswer(response, true);
    }
}
