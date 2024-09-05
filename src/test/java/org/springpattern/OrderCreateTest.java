package org.springpattern;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.Test;
import org.springpattern.order.OrderCreate;

import static org.hamcrest.Matchers.*;

public class OrderCreateTest extends BaseTest {

    @Test
    @Description("Создание заказа с помощью Auth и валидными ингредиентами")
    public void testCreateOrderWithAuthAndValidIngredients() {
        OrderCreate orderCreate = new OrderCreate();
        Response createOrder = orderCreate.createOrderWithAuthAndValidIngredients(accessToken);
        createOrder.then().statusCode(200).body("success", equalTo(true)).body("order.number", notNullValue());

    }

    @Test
    @Description("Создание заказа без Auth")
    public void testCreateOrderWithoutAuth() {
        OrderCreate orderCreate = new OrderCreate();
        Response createOrder = orderCreate.createOrderWithoutAuth();
        createOrder.then().statusCode(200).body("success", equalTo(true));

    }

    @Test
    @Description("Создание заказа без ингредиентов")
    public void testCreateOrderWithoutIngredients() {
        OrderCreate orderCreate = new OrderCreate();
        Response createOrder = orderCreate.createOrderWithoutIngredients(accessToken);
        createOrder.then().statusCode(400).body("success", equalTo(false)).body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @Description("Создание заказа с невалидным хешем ингредиентов")
    public void testCreateOrderWithInvalidIngredientHash() {
        OrderCreate orderCreate = new OrderCreate();
        Response createOrder = orderCreate.createOrderWithInvalidIngredientHash(accessToken);
        createOrder.then().statusCode(500);
    }
}
