package org.springpattern;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class UserAuthorizationTest {

    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site/api/auth";
    private String userEmail = "user0014@yandex.ru";
    private String userPassword = "password";
    private String userName = "Username";
    private String accessToken;

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
    @Description("Авторизация под существующим логином и паролем")
    public void authorizationUser() {
        Response response =
                given()
                        .contentType("application/json")
                        .body("{\"email\": \"" + userEmail + "\", \"password\": \"" + userPassword + "\"}")
                        .post("/login");

        response.then()
                .statusCode(200)
                .body("success", equalTo(true));
    }


    @Test
    @Description("Авторизация без обязательных полей")
    public void userAuthorizationWithoutMandatoryFields() {
        given()
                .contentType("application/json")
                .body("{\"email\": \"" + userEmail + "\", \"password\": \"\"}")
                .post("/login")
                .then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @Description("Авторизация с неверными учетными данными")
    public void loggingInWithIncorrectCredentials() {

            Response response =
                    given()
                            .contentType("application/json")
                            .body("{\"email\": \"" + "new"+userEmail + "\", \"password\": \"" + userPassword + "\"}")
                            .post("/login");

            response.then()
                    .statusCode(401)
                    .body("success", equalTo(false))
                    .body("message", equalTo("email or password are incorrect"));
    }



    public void createUniqueUser() {
        Response response = given()
                .contentType("application/json")
                .body("{\"email\": \"" + userEmail + "\", \"password\": \"" + userPassword + "\", \"name\": \"" + userName + "\"}")
                .when()
                .post("/register");
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
