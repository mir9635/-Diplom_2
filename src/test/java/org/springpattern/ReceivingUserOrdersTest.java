package org.springpattern;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.Test;
import org.springpattern.order.OrderAuthorization;

import static org.hamcrest.Matchers.*;

public class ReceivingUserOrdersTest extends BaseTest {
    @Test
    @Description("Список заказов авторизованного пользователь")
    public void testGetOrdersAuthorizedUser() {
        OrderAuthorization orderAuthorization = new OrderAuthorization();
        Response getOrders = orderAuthorization.getOrdersAuthorizedUser(accessToken);
        getOrders.then().statusCode(200).body("success", is(true)).body("orders", not(emptyArray())).body("total", greaterThan(0));
    }

    @Test
    @Description("Список заказов без авторизации")
    public void testGetOrdersUnauthorizedUser() {
        OrderAuthorization orderAuthorization = new OrderAuthorization();
        Response getOrders = orderAuthorization.getOrdersUnauthorizedUser();
        getOrders.then().statusCode(401).body("success", is(false)).body("message", equalTo("You should be authorised")); // Проверка сообщения об ошибке
    }
}
