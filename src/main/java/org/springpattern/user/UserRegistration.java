package org.springpattern.user;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class UserRegistration {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site/api/auth";
    private static final String REGISTRATION_URL = "/register";
    User user;

    @Step("Получение данных при создание пользователя")
    public Response createUniqueUser(User user) {
        this.user = user;
        return given()
                .baseUri(BASE_URL)
                .contentType("application/json")
                .body(user)
                .when()
                .post(REGISTRATION_URL);
    }


    public String authorizationUser() {
        Response response =
                given()
                        .contentType("application/json")
                        .body("{\"email\": \"" + user.getEmail() + "\", \"password\": \"" + user.getPassword() + "\"}")
                        .post("/login");

        response.then()
                .statusCode(200)
                .body("success", equalTo(true));
        return response.jsonPath().getString("accessToken");
    }

    public void deleteUser(String accessToken) {
        Response response = given()
                .header("Authorization", accessToken)
                .delete("/user"); // Использование относительного URL

        if (response.statusCode() != 202) {
            System.err.println("Ошибка при удалении пользователя: " + response.body().asString());
        }
    }
}
