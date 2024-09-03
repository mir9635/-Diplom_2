package org.springpattern;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class UserRegistrationTest {

    //private static final String BASE_URL = "https://stellarburgers.nomoreparties.site/api/auth/register";
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site/api/auth";
    private String userEmail = "user0014@yandex.ru";
    private String userPassword = "password";
    private String userName = "Username";
    private String accessToken;
    //private Boolean userCreated = false;

    @Before
    public void setup() {
        RestAssured.baseURI = BASE_URL;
    }

    @After
    public void cleanup() {
        if (accessToken != null) {
            deleteUser();
        }
    }

    @Test
    @Description("Создание уникального пользователя")
    public void createUniqueUser() {
        given()
                .contentType("application/json")
                .body("{\"email\": \"" + userEmail + "\", \"password\": \"" + userPassword + "\", \"name\": \"" + userName + "\"}")
                .when()
                .post("/register")
                .then()
                .statusCode(200)
                .body("success", equalTo(true));
        authorizationUser();

    }

    @Test
    @Description("Создание пользователя, который уже зарегистрирован")
    public void createExistingUser() {
        // Создаем первого пользователя
        given()
                .contentType("application/json")
                .body("{\"email\": \"" + userEmail + "\", \"password\": \"" + userPassword + "\", \"name\": \"" + userName + "\"}")
                .post("/register");

        // Пытаемся создать его же снова
        given()
                .contentType("application/json")
                .body("{\"email\": \"" + userEmail + "\", \"password\": \"" + userPassword + "\", \"name\": \"" + userName + "\"}")
                .post("/register")
                .then().statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
        authorizationUser();
    }

    @Test
    @Description("Создание пользователя без обязательных полей")
    public void createUserWithoutRequiredFields() {
        given()
                .contentType("application/json")
                .body("{\"email\": \"" + userEmail + "\", \"name\": \"" + userName + "\"}") // Пароль отсутствует
                .post("/register")
                .then().statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    private void authorizationUser() {
        Response response =
                given()
                        .contentType("application/json")
                        .body("{\"email\": \"" + userEmail + "\", \"password\": \"" + userPassword + "\"}")
                        .post("/login");

        response.then()
                .statusCode(200)
                .body("success", equalTo(true));
        accessToken = response.jsonPath().getString("accessToken");
    }




    private void deleteUser() {
        Response response = given()
                .header("Authorization", accessToken)
                .delete("/user"); // Использование относительного URL

        if (response.statusCode() != 202) {
            System.err.println("Ошибка при удалении пользователя: " + response.body().asString());
        }
    }
}
