package org.springpattern;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.Test;
import org.springpattern.user.User;
import org.springpattern.user.UserAuthorization;

import static org.hamcrest.Matchers.equalTo;

public class UserAuthorizationTest extends BaseTest {

    @Test
    @Description("Авторизация под существующим логином и паролем")
    public void testAuthorizationUser() {
        UserAuthorization userAuthorization = new UserAuthorization();
        User user = new User();
        user.setEmail(userEmail);
        user.setPassword(userPassword);

        Response authorizationUser = userAuthorization.authorizationUser(user);
        authorizationUser.then().statusCode(200).body("success", equalTo(true));
    }


    @Test
    @Description("Авторизация без обязательных полей")
    public void testUserAuthorizationWithoutMandatoryFields() {
        UserAuthorization userAuthorization = new UserAuthorization();
        User user = new User();
        user.setEmail(userEmail);
        user.setPassword("");

        Response authorizationUser = userAuthorization.userAuthorizationWithoutMandatoryFields(user);
        authorizationUser.then().statusCode(401).body("success", equalTo(false)).body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @Description("Авторизация с неверными учетными данными")
    public void testLoggingInWithIncorrectCredentials() {

        UserAuthorization userAuthorization = new UserAuthorization();
        User user = new User();
        user.setEmail("new" + userEmail);
        user.setPassword(userPassword);

        Response authorizationUser = userAuthorization.loggingInWithIncorrectCredentials(user);
        authorizationUser.then().statusCode(401).body("success", equalTo(false)).body("message", equalTo("email or password are incorrect"));
    }
}
