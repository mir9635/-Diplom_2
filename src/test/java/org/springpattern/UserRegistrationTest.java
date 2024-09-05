package org.springpattern;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springpattern.user.User;
import org.springpattern.user.UserRegistration;

import static org.hamcrest.Matchers.equalTo;

public class UserRegistrationTest extends BaseTest {

    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site/api/auth";
    private UserRegistration userRegistration;

    @Override
    @Before
    public void setup() {
        RestAssured.baseURI = BASE_URL;
    }

    @Override
    @After
    public void cleanup() {
        if (accessToken != null) {
            userRegistration.deleteUser(accessToken);
        }
    }

    @Test
    @Description("Создание уникального пользователя")
    public void testCreateUniqueUser() {
        userRegistration = new UserRegistration();
        Response registrationUser = userRegistration.createUniqueUser(new User(userEmail, userPassword, userName));

        accessToken = userRegistration.authorizationUser();
        registrationUser.then().statusCode(200).body("success", equalTo(true));
    }

    @Test
    @Description("Создание пользователя, который уже зарегистрирован")
    public void testCreateExistingUser() {
        userRegistration = new UserRegistration();
        userRegistration.createUniqueUser(new User(userEmail, userPassword, userName));
        Response registrationUser = userRegistration.createUniqueUser(new User(userEmail, userPassword, userName));

        accessToken = userRegistration.authorizationUser();
        registrationUser.then().statusCode(403).body("success", equalTo(false)).body("message", equalTo("User already exists"));
    }

    @Test
    @Description("Создание пользователя без обязательных полей")
    public void testCreateUserWithoutRequiredFields() {
        userRegistration = new UserRegistration();
        Response registrationUser = userRegistration.createUniqueUser(new User(userEmail, userName));
        registrationUser.then().statusCode(403).body("success", equalTo(false)).body("message", equalTo("Email, password and name are required fields"));
    }

}
