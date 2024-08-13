package order;

import edu.practikum.dto.order.OrderResponse;
import edu.practikum.dto.user.UserResponse;
import edu.practikum.util.BaseScenario;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static edu.practikum.test.data.Ingredient.setIngredients;
import static edu.practikum.test.data.Ingredient.setWrongIngredients;
import static edu.practikum.test.data.OrderAnswer.getExpectedApiResponse;
import static edu.practikum.test.data.OrderAnswer.getExpectedApiResponseWithoutToken;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DisplayName("Создание заказа")
public class CreateOrderTest extends BaseScenario {

    public static final String ERROR_MESSAGE = "Ingredient ids must be provided";
    private final static String API_PATH = CONNECTION_PROPERTIES.getOrderPath();
    private String token;
    private Response responseBefore;
    private UserResponse userResponseBefore;

    @BeforeEach
    public void getActiveToken() {
        //Создадим стандартного пользователя и сразу получим его токен. Т.к. больше этот user в тесте не нужен
        responseBefore = createUniqueUser();
        token = responseBefore.getHeader("Authorization");
        userResponseBefore = responseBefore.getBody().as(UserResponse.class);
    }

    @AfterEach
    public void deleteUser() {
        deleteUser(responseBefore);
    }

    @Test
    @DisplayName("Успешное создание заказа")
    @Description("Создание заказа по ингредиентам")
    public void createOrderTest() {

        /*
            Что тут происходит:
            setIngredients() - возвращаем объект с id ингредиентов для заказа и с этим списком создаем заказ
            Преобразуем ответ метода в POJO объект OrderResponse
            getExpectedApiResponse() - возвращает ожидаемый ответ метода после создания заказа.
                 userResponseBefore в него передается для того чтобы мы могли удостовериться что заказ
                 будет для того пользователя, который его создавал.
                 Id заказа, его номер и время рассчитать и сравнить не получится, поэтому в DTO классах объекта
                 была использована аннотация @EqualsAndHashCode.Exclude над полями, которые не будут участвовать в сравнении
                 Это позволяет не перебирать все поля по одному, а сравнить сразу 2 обьекта через isEqualTo
                 Также, для того чтобы так сравнить было можно, AssertThat был подтянут из assertj вместо пакета hamcrest

                 Поля которые были исключены из сравнения проверяются отдельно
         */

        Response response = sendPostRequest("Создаем бургер", token, setIngredients(), API_PATH);
        //Проверили код ответа
        assertThat(response.getStatusCode()).isEqualTo(SC_OK);
        //Если код ответа корректен начинаем преобразование
        OrderResponse orderResponse = response.getBody().as(OrderResponse.class);
        //Проверяем ответ с подготовленным эталоном
        assertThat(orderResponse).isEqualTo(getExpectedApiResponse(userResponseBefore));

        //Проверяем что в ответе пришли поля, которые исключали ранее
        assertThat(orderResponse.getOrder().get_id()).isNotBlank();
        assertThat(orderResponse.getOrder().getNumber()).isNotNull();
        assertThat(orderResponse.getOrder().getCreatedAt()).isNotBlank();
        assertThat(orderResponse.getOrder().getUpdatedAt()).isNotBlank();
        assertThat(orderResponse.getOrder().getOwner().getCreatedAt()).isNotBlank();
        assertThat(orderResponse.getOrder().getOwner().getUpdatedAt()).isNotBlank();

    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Попытка создать заказ не авторизованным пользователем")
    public void createOrderWithoutTokenTest() {

        Response response = sendPostRequest("Создаем бургер", setIngredients(), API_PATH);

        //Проверили код ответа
        assertThat(response.getStatusCode()).isEqualTo(SC_OK);
        //Если код ответа корректен начинаем преобразование
        OrderResponse orderResponse = response.getBody().as(OrderResponse.class);
        //Проверяем ответ с подготовленным эталоном
        assertThat(orderResponse).isEqualTo(getExpectedApiResponseWithoutToken());
        //Проверяем что в ответе пришел номер заказа. т.к. в проверке выше он пропускается из-за аннотации
        assertThat(orderResponse.getOrder().getNumber()).isNotNull();
    }


    @Test
    @DisplayName("Попытка создать заказ без ингредиента")
    @Description("Создание заказа без ингредиентов")
    public void createOrderWithoutIngredientTest() {
        Response response = sendPostRequest("Создаем бургер", token, "", API_PATH);

        OrderResponse orderResponse = response.getBody().as(OrderResponse.class);

        MatcherAssert.assertThat("Статус код не соответствует ожидаемому", SC_BAD_REQUEST, equalTo(response.getStatusCode()));
        MatcherAssert.assertThat("Флаг пришел false", false, equalTo(orderResponse.isSuccess()));
        MatcherAssert.assertThat("Email не совпадает", ERROR_MESSAGE, equalTo(orderResponse.getMessage()));
    }

    @Test
    @DisplayName("Попытка создать заказ с существующим ингредиентом")
    @Description("Создание заказа с существующим ингредиентом")
    public void createOrderWithWrongIngredientTest() {

        sendPostRequest("Создаем бургер", token, setWrongIngredients(), API_PATH)
                .then()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }

}
