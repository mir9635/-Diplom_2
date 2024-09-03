package org.springpattern;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ChangingUserDataTest extends BaseTest {

    private String userEmail = "user0014@yandex.ru";
    private String userPassword = "password";
    private String userName = "Username";
    private String accessToken;
    private String repeatUserEmail = "user0050@yandex.ru";
    private String secondAccessToken;
    private Boolean repeateUserEmail = false;

    @After
    public void cleanup() {
        deleteUser();
        if (repeateUserEmail) {
            deleteUser2();
        }
    }

    @Test
    @Description("Проверка того, что данные пользователя можно получить")
    public void testGetUserInfoWithAuth() {
        createUniqueUser();
        Response response = given()
                .header("Authorization", accessToken)
                .get("/user");

        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user", notNullValue());
    }

    @Test
    @Description("Проверка получения информации о пользователе без авторизации")
    public void testGetUserInfoWithoutAuth() {
        createUniqueUser();
        Response response = given()
                .get("/user");

        response.then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }


    @Test
    @Description("Обновление информации о пользователе с помощью Auth")
    public void updateUserInfoWithAuth() {
        createUniqueUser();
        String newEmail = "user0050@yandex.ru";
        String newName = "NewName";

        Response response = given()
                .header("Authorization", accessToken)
                .contentType("application/json")
                .body("{\"email\":\"" + newEmail + "\", \"name\":\"" + newName + "\"}")
                .patch("/user");

        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo(newEmail))
                .body("user.name", equalTo(newName));
    }

    @Test
    @Description("Обновление информации о пользователе без Auth")
    public void updateUserInfoWithoutAuth() {
        createUniqueUser();
        String newEmail = "newemail@example.com";
        String newName = "New Name";

        Response response = given()
                .contentType("application/json")
                .body("{\"email\":\"" + newEmail + "\", \"name\":\"" + newName + "\"}")
                .patch("/user");

        response.then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    @Description("Обновление информации о пользователе с помощью существующей электронной почты")
    public void updateUserInfoWithExistingEmail() {
        createUniqueUser();
        repeateUserEmail = true;
        createUniqueUser2();
        String newName = "NewName";

        Response response = given()
                .header("Authorization", accessToken)
                .contentType("application/json")
                .body("{\"email\":\"" + repeatUserEmail + "\", \"name\":\"" + newName + "\"}")
                .patch("/user");

        response.then()
                .statusCode(403)
                .body("success", equalTo(false));


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

    public void createUniqueUser2() {
        Response response = given()
                .contentType("application/json")
                .body("{\"email\": \"" + repeatUserEmail + "\", \"password\": \"" + userPassword + "\", \"name\": \"" + userName + "\"}")
                .when()
                .post("/register");
        response.then()
                .statusCode(200)
                .body("success", equalTo(true));
        secondAccessToken = response.jsonPath().getString("accessToken");
    }

    private void deleteUser2() {
        Response response = given()
                .header("Authorization", secondAccessToken)
                .delete("/user"); // Использование относительного URL

        if (response.statusCode() != 202) {
            System.err.println("Ошибка при удалении пользователя: " + response.body().asString());
        }
    }
}
