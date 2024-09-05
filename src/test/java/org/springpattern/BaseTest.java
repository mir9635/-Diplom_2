package org.springpattern;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.springpattern.user.User;
import org.springpattern.user.UserRegistration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class BaseTest {

    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site/api";
    protected String userEmail = "user0021@yandex.ru";
    protected String userPassword = "password";
    protected String userName = "Username";
    protected String accessToken;
    protected String repeatUserEmail = "user0052@yandex.ru";
    protected String secondAccessToken;
    protected Boolean repeateUserEmail = false;


    @Before
    public void setup() {
        RestAssured.baseURI = BASE_URL;

        UserRegistration userRegistration = new UserRegistration();
        Response response = userRegistration.createUniqueUser(new User(userEmail, userPassword, userName));
        response.then().statusCode(200).body("success", equalTo(true));
        accessToken = response.jsonPath().getString("accessToken");
    }

    @After
    public void cleanup() {
        deleteUser(accessToken);
        if (repeateUserEmail) {
            deleteUser(secondAccessToken);
        }
    }

    private void deleteUser(String token) {
        Response response = given()
                .header("Authorization", token)
                .delete("/auth/user");

        if (response.statusCode() != 202) {
            System.err.println("Ошибка при удалении пользователя: " + response.body().asString());
        }
    }


}
