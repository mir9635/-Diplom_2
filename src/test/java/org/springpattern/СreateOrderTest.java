package org.springpattern;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class СreateOrderTest {

    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site/api";
    private String userEmail = "user0014@yandex.ru";
    private String userPassword = "password";
    private String userName = "Username";
    private String accessToken;


    private String validIngredient1;
    private String validIngredient2;

    @Before
    public void setup() {
        RestAssured.baseURI = BASE_URL;
        createUniqueUser();
    }

    @After
    public void cleanup() {
        deleteUser();
    }


    @Test
    @Description("Создание заказа с помощью Auth и валидными ингредиентами")
    public void createOrderWithAuthAndValidIngredients() {
        getIngredients();
        String requestBody = "{ \"ingredients\": [ \"" + validIngredient1 + "\", \"" + validIngredient2 + "\" ] }";

        Response response = given()
                .header("Authorization", accessToken)
                .contentType("application/json")
                .body(requestBody)
                .post("/orders");
        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("order.number", notNullValue());

    }

    @Test
    @Description("Создание заказа без Auth")
    public void createOrderWithoutAuth() {
        getIngredients();
        String requestBody = "{ \"ingredients\": [ \"" + validIngredient1 + "\", \"" + validIngredient2 + "\" ] }";

        Response response = given()
                .contentType("application/json")
                .body(requestBody)
                .post("https://stellarburgers.nomoreparties.site/api/orders");
        response.then()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @Description("Создание заказа без ингредиентов")
    public void createOrderWithoutIngredients() {
        String requestBody = "{ }";

        Response response = given()
                .header("Authorization", accessToken)
                .contentType("application/json")
                .body(requestBody)
                .post("/orders");

        response.then()
                .statusCode(400)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }


    @Test
    @Description("Создание заказа с невалидным хешем ингредиентов")
    public void createOrderWithInvalidIngredientHash() {
        String requestBody = "{ \"ingredients\": [ \"" + "45d5ghf6454" + "\" ] }";


        Response response = given()
                .header("Authorization", accessToken)
                .contentType("application/json")
                .body(requestBody)
                .post("/orders");
        response.then()
                .statusCode(500);
    }


    public void getIngredients() {

        Response ingredientResponse = given()
                .contentType("application/json")
                .get("/ingredients");
        // Получаем данные
        List<String> ingredientIds = ingredientResponse.jsonPath().getList("data._id");

        // Генератор случайных чисел
        Random random = new Random();

        // Проверяем, что в списке достаточно элементов
        if (ingredientIds.size() >= 2) {
            // Получаем два случайных индекса
            int index1 = random.nextInt(ingredientIds.size());
            int index2;
            do {
                index2 = random.nextInt(ingredientIds.size());
            } while (index2 == index1); // Убедимся, что индексы разные

            // Присваиваем значения переменным
            validIngredient1 = ingredientIds.get(index1);
            validIngredient2 = ingredientIds.get(index2);

        }
    }




    public void createUniqueUser() {
        Response response = given()
                .contentType("application/json")
                .body("{\"email\": \"" + userEmail + "\", \"password\": \"" + userPassword + "\", \"name\": \"" + userName + "\"}")
                .when()
                .post("/auth/register");
        response.then()
                .statusCode(200)
                .body("success", equalTo(true));
        accessToken = response.jsonPath().getString("accessToken");

    }

    private void deleteUser() {
        Response response = given()
                .header("Authorization", accessToken)
                .delete("/auth/user"); // Использование относительного URL

        if (response.statusCode() != 202) {
            System.err.println("Ошибка при удалении пользователя: " + response.body().asString());
        }
    }
}
