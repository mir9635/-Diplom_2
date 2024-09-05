package org.springpattern.user;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserAuthorization {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site/api/auth";
    private static final String AUTHORIZATION_URL = "/login";

    @Step("Получение данных под существующим логином и паролем")
    public Response authorizationUser(User user) {
        return given()
                .baseUri(BASE_URL)
                .contentType("application/json")
                .body(user)
                .post(AUTHORIZATION_URL);
    }

    @Step("Получение данных без пароля")
    public Response userAuthorizationWithoutMandatoryFields(User user) {
        return given()
                .baseUri(BASE_URL)
                .contentType("application/json")
                .body(user)
                .post(AUTHORIZATION_URL);
    }

    @Step("Получение данных при авторизации с неверными учетными данными")
    public Response loggingInWithIncorrectCredentials(User user) {
        return given()
                .baseUri(BASE_URL)
                .contentType("application/json")
                .body(user)
                .post(AUTHORIZATION_URL);
    }
}
