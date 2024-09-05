package org.springpattern;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.Test;
import org.springpattern.user.User;
import org.springpattern.user.UserData;
import org.springpattern.user.UserRegistration;

import static org.hamcrest.Matchers.*;

public class ChangingUserDataTest extends BaseTest {

    @Test
    @Description("Проверка того, что данные пользователя можно получить")
    public void testGetUserInfoWithAuth() {
        UserData userData = new UserData();
        Response createUser = userData.getUserInfoWithAuth(accessToken);
        createUser.then().statusCode(200).body("success", equalTo(true)).body("user", notNullValue());
    }

    @Test
    @Description("Проверка получения информации о пользователе без авторизации")
    public void testGetUserInfoWithoutAuth() {
        UserData userData = new UserData();
        Response createUser = userData.getUserInfoWithoutAuth();
        createUser.then().statusCode(401).body("success", equalTo(false)).body("message", equalTo("You should be authorised"));
    }


    @Test
    @Description("Обновление информации о пользователе с помощью Auth")
    public void testUpdateUserInfoWithAuth() {
        String newEmail = "user0051@yandex.ru";
        String newName = "NewName";
        UserData userData = new UserData();
        Response createUser = userData.updateUserInfoWithAuth(accessToken, new User(newEmail, newName));
        createUser.then().statusCode(200).body("success", equalTo(true)).body("user.email", equalTo(newEmail)).body("user.name", equalTo(newName));
    }

    @Test
    @Description("Обновление информации о пользователе без Auth")
    public void testUpdateUserInfoWithoutAuth() {
        String newEmail = "newemail@example.com";
        String newName = "New Name";
        UserData userData = new UserData();
        Response createUser = userData.updateUserInfoWithoutAuth(new User(newEmail, newName));
        createUser.then().statusCode(401).body("success", equalTo(false)).body("message", equalTo("You should be authorised"));

    }

    @Test
    @Description("Обновление информации о пользователе с помощью существующей электронной почты")
    public void testUpdateUserInfoWithExistingEmail() {
        repeateUserEmail = true;

        UserRegistration userRegistration = new UserRegistration();
        Response response = userRegistration.createUniqueUser(new User(repeatUserEmail, userPassword, userName));
        response.then().statusCode(200).body("success", equalTo(true));
        secondAccessToken = response.jsonPath().getString("accessToken");

        String repeatUserEmail = "user0052@yandex.ru";
        String newName = "NewName";

        UserData userData = new UserData();
        Response createUser = userData.updateUserInfoWithExistingEmail(accessToken, new User(repeatUserEmail, newName));
        createUser.then().statusCode(403).body("success", equalTo(false));
    }

}
