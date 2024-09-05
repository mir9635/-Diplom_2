package org.springpattern.order;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;

public class OrderCreate {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site/api";
    private static final String ORDER_URL = "/orders";

    private String validIngredient1;
    private String validIngredient2;

    @Step("Получение списка заказов с помощью Auth и валидными ингредиентами")
    public Response createOrderWithAuthAndValidIngredients(String accessToken) {
        getIngredients();
        String requestBody = "{ \"ingredients\": [ \"" + validIngredient1 + "\", \"" + validIngredient2 + "\" ] }";
        return given()
                .baseUri(BASE_URL)
                .header("Authorization", accessToken)
                .contentType("application/json")
                .body(requestBody)
                .post(ORDER_URL);

    }

    @Step("Получение списка заказов без Auth и валидными ингредиентами")
    public Response createOrderWithoutAuth() {
        getIngredients();
        String requestBody = "{ \"ingredients\": [ \"" + validIngredient1 + "\", \"" + validIngredient2 + "\" ] }";
        return given()
                .baseUri(BASE_URL)
                .contentType("application/json")
                .body(requestBody)
                .post(ORDER_URL);
    }

    @Step("Получение списка заказов с помощью Auth и без ингредиентов")
    public Response createOrderWithoutIngredients(String accessToken) {
        String requestBody = "{ }";
        return given()
                .baseUri(BASE_URL)
                .header("Authorization", accessToken)
                .contentType("application/json")
                .body(requestBody)
                .post(ORDER_URL);
    }

    @Step("Получение списка заказов с помощью Auth и не валидными ингредиентами")
    public Response createOrderWithInvalidIngredientHash(String accessToken) {
        String requestBody = "{ \"ingredients\": [ \"" + "45d5ghf6454" + "\" ] }";
        return given()
                .baseUri(BASE_URL)
                .header("Authorization", accessToken)
                .contentType("application/json")
                .body(requestBody)
                .post(ORDER_URL);
    }

    public void getIngredients() {

        Response ingredientResponse = given()
                .contentType("application/json")
                .get("/ingredients");
        List<String> ingredientIds = ingredientResponse.jsonPath().getList("data._id");

        Random random = new Random();


        if (ingredientIds.size() >= 2) {
            int index1 = random.nextInt(ingredientIds.size());
            int index2;
            do {
                index2 = random.nextInt(ingredientIds.size());
            } while (index2 == index1);

            validIngredient1 = ingredientIds.get(index1);
            validIngredient2 = ingredientIds.get(index2);

        }
    }
}
