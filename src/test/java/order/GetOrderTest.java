package order;

import edu.practikum.dto.order.OrderForUserResponse;
import edu.practikum.util.BaseScenario;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static edu.practikum.test.data.Ingredient.setIngredients;
import static edu.practikum.test.data.OrderAnswer.getOrderForAuthorizedUser;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Получение заказа")
public class GetOrderTest extends BaseScenario {

    public static final String ERROR_MESSAGE = "You should be authorised";
    private final static String API_PATH = CONNECTION_PROPERTIES.getOrderPath();
    private String token;
    private Response responseBefore;

    @BeforeEach
    public void getActiveToken() {
        //Создадим стандартного пользователя и сразу получим его токен. Т.к. больше этот user в тесте не нужен
        responseBefore = createUniqueUser();
        token = responseBefore.getHeader("Authorization");
    }

    @AfterEach
    public void deleteUser() {
        deleteUser(responseBefore);
    }

    @Test
    @DisplayName("Запрос заказов без авторизации")
    @Description("Запрос заказа без авторизации")
    public void getOrderWithoutTokenTest() {
        Response response = sendEasyGetRequest("Запрос заказов без авторизации", API_PATH);

        checkEasyResponse(response, SC_UNAUTHORIZED, "success", "message", false,
                ERROR_MESSAGE, "Проверка ответа при создании дубля");
    }

    @Test
    @DisplayName("Запрос заказов с авторизацией")
    @Description("Запрос заказа с авторизацией")
    public void getOrderWithTokenTest() {


        //создать заказ для авторизованного пользователя чтобы в ответе что то возвращалось
        sendPostRequest("Создаем бургер", token, setIngredients(), API_PATH);

        //получаем заказ
        Response response = sendEasyGetRequest("Запрос заказов с авторизацией", token, API_PATH);

        assertThat(response.getStatusCode()).isEqualTo(SC_OK);
        //Если код ответа корректен начинаем преобразование
        OrderForUserResponse orderResponse = response.getBody().as(OrderForUserResponse.class);
        //Проверяем ответ с подготовленным эталоном
        assertThat(orderResponse).isEqualTo(getOrderForAuthorizedUser());

        //Проверяем что в ответе пришли поля, которые исключали ранее
        assertThat(orderResponse.getTotalToday()).isNotNull();
        assertThat(orderResponse.getTotal()).isNotNull();

        assertThat(orderResponse.getOrders().get(0).get_id()).isNotBlank();
        assertThat(orderResponse.getOrders().get(0).getCreatedAt()).isNotBlank();
        assertThat(orderResponse.getOrders().get(0).getUpdatedAt()).isNotBlank();
        assertThat(orderResponse.getOrders().get(0).getNumber()).isNotNull();
    }

}
