package org.springpattern;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class BaseTest {

    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site/api/auth";
    private String userEmail = "user0014@yandex.ru";
    private String userPassword = "password";
    private String userName = "Username";
    private String accessToken;
    private String repeatUserEmail = "user0050@yandex.ru";
    private String secondAccessToken;
    private Boolean repeateUserEmail = false;


    @Before
    public void setup() {
        RestAssured.baseURI = BASE_URL;
    }



}
