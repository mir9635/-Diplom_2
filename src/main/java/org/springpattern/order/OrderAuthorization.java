package org.springpattern.order;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;


public class OrderAuthorization {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site/api";
    private static final String ORDER_URL = "/orders";

    @Step("Получить список заказов авторизованного пользователь")
    public Response getOrdersAuthorizedUser(String accessToken) {
        return given()
                .baseUri(BASE_URL)
                .header("Authorization", accessToken)
                .when()
                .get(ORDER_URL);
    }

    @Step("Получить список заказов без авторизации")
    public Response getOrdersUnauthorizedUser() {
        return given()
                .baseUri(BASE_URL)
                .when()
                .get(ORDER_URL);
    }
}
