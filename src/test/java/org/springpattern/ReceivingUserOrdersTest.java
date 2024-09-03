package org.springpattern;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ReceivingUserOrdersTest {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site/api";
    private String userEmail = "user0015@yandex.ru";
    private String userPassword = "password";
    private String userName = "Username";
    private String accessToken;


    private String validIngredient1;// = "60d3b41abdacab0026a733c6"; // пример валидного хеша
    private String validIngredient2;// = "61c0c5a71d1f82001bdaaa6d"; // пример валидного хеша
    private String invalidIngredient = "invalid_hash";

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
    public void getOrdersAuthorizedUser() {
        // Тест для авторизованного пользователя
        Response response = given()
                .header("Authorization", accessToken)
                .when()
                .get("/orders");
        response
                .then()
                .statusCode(200) // Проверка успешного ответа
                .body("success", is(true))
                .body("orders", not(emptyArray())) // Проверка наличия заказов
                .body("total", greaterThan(0)); // Проверка общего количества заказов
    }

    @Test
    public void testGetOrdersUnauthorizedUser() {
        // Тест для неавторизованного пользователя
        Response response = given()
                .when()
                .get("/orders");
        response
                .then()
                .statusCode(401) // Ожидаем код ответа 401
                .body("success", is(false))
                .body("message", equalTo("You should be authorised")); // Проверка сообщения об ошибке
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
