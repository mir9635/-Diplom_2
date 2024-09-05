package org.springpattern.user;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserData {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site/api/auth";
    private static final String USER_URL = "/user";

    @Step("Получение данных пользователя с авторизацией")
    public Response getUserInfoWithAuth(String accessToken) {
        return given()
                .baseUri(BASE_URL)
                .header("Authorization", accessToken)
                .get(USER_URL);


    }

    @Step("Получение данных пользователя без авторизацией")
    public Response getUserInfoWithoutAuth() {
        return given()
                .baseUri(BASE_URL)
                .get(USER_URL);
    }

    @Step("Получение данных пользователя с авторизацией для обновления информации")
    public Response updateUserInfoWithAuth(String accessToken, User user) {
        return given()
                .baseUri(BASE_URL)
                .header("Authorization", accessToken)
                .contentType("application/json")
                .body(user)
                .patch(USER_URL);
    }

    @Step("Получение данных пользователя без авторизацией для обновления информации")
    public Response updateUserInfoWithoutAuth(User user) {
        return given()
                .baseUri(BASE_URL)
                .contentType("application/json")
                .body(user)
                .patch(USER_URL);
    }

    @Step("Получение информации о пользователе при обновлении электронной почты на существующею")
    public Response updateUserInfoWithExistingEmail(String accessToken, User user) {
        return given()
                .baseUri(BASE_URL)
                .header("Authorization", accessToken)
                .contentType("application/json")
                .body(user)
                .patch(USER_URL);
    }

}
