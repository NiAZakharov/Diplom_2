package edu.practikum.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import edu.practikum.dto.user.User;
import edu.practikum.dto.user.UserResponse;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import java.util.HashMap;
import java.util.Locale;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;

@Slf4j
public class BaseScenario {

    protected static final ConnectionProperty CONNECTION_PROPERTIES = PropertyLoader.loadProperties();
    private final static String INSUFFICIENT_DATA_MESSAGE = "Недостаточно данных для входа";
    private final static Faker faker = new Faker(new Locale("ru_Ru", "RU"));

    protected static RequestSpecification requestSpecification;
    private final ObjectMapper objectMapper = new ObjectMapper();


    @BeforeAll
    public static void setUp() {
        requestSpecification = RestAssured
                .given()
                .baseUri(CONNECTION_PROPERTIES.getHost())
                .basePath(CONNECTION_PROPERTIES.getBaseApiPath())
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .filter(new AllureRestAssuredFilter())
                .log().all();

        RestAssured.requestSpecification = requestSpecification;
    }


    @AfterAll
    public static void tearDown() {
        RestAssured.reset();
    }

    @Step(value = "Создание нового уникального пользователя")
    public Response createUniqueUser() {
        User newUser = User
                .builder()
                .email(faker.internet().emailAddress())
                .password(faker.internet().password())
                .name(faker.name().username())
                .build();
        return sendPostRequest("Вызов метода создания пользователя", newUser, CONNECTION_PROPERTIES.getUserRegisterPath());
    }

    @Step(value = "{0}")
    public Response sendPostRequest(String promt, Object obj, String urlPath) {
        return given()
                .body(obj)
                .when()
                .post(urlPath);
    }

    @Step(value = "{0}")
    public Response sendPostRequest(String promt, String token, Object obj, String urlPath) {
        return given()
                .header("Authorization", token)
                .body(obj)
                .when()
                .post(urlPath);
    }

    @Step(value = "{0}")
    public UserResponse sendPathUserRequest(String promt, String header, String headerValue, Object obj,
                                            String urlPath, Integer statusCode) {
        return given()
                .when()
                .header(header, headerValue)
                .body(obj)
                .patch(urlPath)
                .then()
                .statusCode(statusCode)
                .extract()
                .as(UserResponse.class);
    }

    @Step(value = "{0}")
    public Response sendEasyGetRequest(String promt, String urlPath, HashMap<String, String> params) {
        return given()
                .params(params)
                .when()
                .get(urlPath);
    }

    @Step(value = "{0}")
    public Response sendEasyGetRequest(String promt, String urlPath) {
        return given()
                .when()
                .get(urlPath);
    }

    public void logResponseToAllure(Response response) {
        //Добавил вручную, т.к. автоматически это почему то не срабатывает. Думаю из за ассерта в цепочке
        Allure.addAttachment("Response Status Code", String.valueOf(response.getStatusCode()));
        Allure.addAttachment("Response Headers", response.getHeaders().toString());
        Allure.addAttachment("Response Body", response.getBody().asPrettyString());
    }

    @Step(value = "{4}")
    public void checkEasyResponse(Response response, int expectedStatusCode,
                                  String bodySingleKey, String expectedValue, String allurePromt) {
        log.info(response.prettyPrint());
        logResponseToAllure(response);
        response.then()
                .statusCode(expectedStatusCode)
                .and()
                .assertThat().body(bodySingleKey, equalTo(expectedValue));
    }

    @Step(value = "{4}")
    public void checkEasyResponse(Response response, int expectedStatusCode,
                                  String bodySingleKey, Boolean expectedValue, String allurePromt) {
        log.info(response.prettyPrint());
        logResponseToAllure(response);
        response.then()
                .statusCode(expectedStatusCode)
                .and()
                .assertThat().body(bodySingleKey, equalTo(expectedValue));
    }

    @Step(value = "{4}")
    public void checkEasyResponse(Response response, int expectedStatusCode,
                                  String bodySingleKey1, String bodySingleKey2, Boolean expectedValue1,
                                  String expectedValue2, String allurePromt) {
        log.info(response.prettyPrint());
        logResponseToAllure(response);
        response.then()
                .assertThat()
                .body(bodySingleKey1, equalTo(expectedValue1))
                .and()
                .body(bodySingleKey2, equalTo(expectedValue2));
    }

    @Step(value = "{4}")
    public void checkEasyResponse(Response response, int expectedStatusCode,
                                  String bodySingleKey, String allurePromt) {
        log.info(response.prettyPrint());
        logResponseToAllure(response);
        response.then()
                .statusCode(expectedStatusCode)
                .and()
                .assertThat().body(bodySingleKey, Matchers.any(Integer.class));
    }

    @Step(value = "Удаление пользователя/Очистка данных")
    public Response deleteUser(Response response) {

        UserResponse userResponse = response.getBody().as(UserResponse.class);

        User deleteUser = User
                .builder()
                .email(userResponse.getUser().getEmail())
                .password(userResponse.getUser().getPassword())
                .name(userResponse.getUser().getName())
                .build();

        return given()
                .header("Authorization", response.header("Authorization"))
                .body(deleteUser)
                .when()
                .delete(CONNECTION_PROPERTIES.getUserGetPatchPath());
    }
}
