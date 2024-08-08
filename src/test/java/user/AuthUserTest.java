package user;

import com.github.javafaker.Faker;
import edu.practikum.dto.user.User;
import edu.practikum.dto.user.UserResponse;
import edu.practikum.util.BaseScenario;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;

@DisplayName("Авторизация пользователя")
public class AuthUserTest extends BaseScenario {

    private final static String API_PATH = CONNECTION_PROPERTIES.getUserLoginPath();
    private final static String ERROR_MESSAGE = "email or password are incorrect";
    private final static Faker FAKER = new Faker(new Locale("ru_Ru", "RU"));
    private Response responseBefore;
    private User userBefore;

    @BeforeEach
    public void createUserAndGetResp() {
        userBefore = User
                .builder()
                .email(FAKER.internet().emailAddress())
                .password(FAKER.internet().password())
                .name(FAKER.name().username())
                .build();

         responseBefore =
                sendPostRequest("Вызов метода создания пользователя", userBefore, CONNECTION_PROPERTIES.getUserRegisterPath());
    }

    @AfterEach
    public void deleteUser() {
        deleteUser(responseBefore);
    }

    @Test
    @DisplayName("Успешная авторизация")
    @Description("Авторизация существующим пользователем")
    public void userCanLoginTest() {

        //объект для авторизации
        User newUser = User
                .builder()
                .email(userBefore.getEmail())
                .password(userBefore.getPassword())
                .name(userBefore.getName())
                .build();

        Response response = sendPostRequest("Авторизация существующим пользователем", newUser, API_PATH);
        UserResponse userResponse = response.getBody().as(UserResponse.class);

        //проверили статус код
        assertThat("Статус код не соответствует ожидаемому", SC_OK, equalTo(response.getStatusCode()));

        //проверили тело ответа
        assertThat("Флаг пришел false", true, equalTo(userResponse.isSuccess()));
        assertThat("Email не совпадает",newUser.getEmail(), equalTo(userResponse.getUser().getEmail()));
        assertThat("Имя не совпадает",newUser.getName(), equalTo(userResponse.getUser().getName()));
        assertThat("Токен не получен", userResponse.getAccessToken(), not(blankOrNullString()));
        assertThat("Полученный токен не валиден", userResponse.getAccessToken(), startsWith("Bearer"));

    }

    @Test
    @DisplayName("Не успешная авторизация")
    @Description("Авторизация пользователем не зарегистрированным")
    public void userCanNotLoginTest() {

        User user = User
                .builder()
                .email(FAKER.internet().emailAddress())
                .password(FAKER.internet().password())
                .name(FAKER.name().username())
                .build();

        Response response = sendPostRequest("Авторизация без пароля", user, API_PATH);
        UserResponse userResponse = response.getBody().as(UserResponse.class);

        //проверили статус код
        assertThat("Статус код не соответствует ожидаемому", SC_UNAUTHORIZED, equalTo(response.getStatusCode()));
        assertThat("Флаг пришел false", false, equalTo(userResponse.isSuccess()));
        assertThat("Email не совпадает",ERROR_MESSAGE, equalTo(userResponse.getMessage()));
    }


    @Test
    @DisplayName("Не успешная авторизация")
    @Description("Авторизация пользователем без пароля")
    public void userLoginWithoutPasswordTest() {

        UserResponse userResponse = responseBefore.getBody().as(UserResponse.class);
        Response response = sendPostRequest("Авторизация без пароля", userResponse, API_PATH);
        userResponse = response.getBody().as(UserResponse.class);

        //проверили статус код
        assertThat("Статус код не соответствует ожидаемому", SC_UNAUTHORIZED, equalTo(response.getStatusCode()));
        assertThat("Флаг пришел false", false, equalTo(userResponse.isSuccess()));
        assertThat("Email не совпадает",ERROR_MESSAGE, equalTo(userResponse.getMessage()));
    }

    @Test
    @DisplayName("Не успешная авторизация")
    @Description("Авторизация пользователем без логина")
    public void userLoginWithoutEmailTest() {

        UserResponse userResponse = responseBefore.getBody().as(UserResponse.class);
        userResponse.getUser().setEmail("");
        Response response = sendPostRequest("Авторизация без пароля", userResponse, API_PATH);
        userResponse = response.getBody().as(UserResponse.class);

        //проверили статус код
        assertThat("Статус код не соответствует ожидаемому", SC_UNAUTHORIZED, equalTo(response.getStatusCode()));
        assertThat("Флаг пришел false", false, equalTo(userResponse.isSuccess()));
        assertThat("Email не совпадает",ERROR_MESSAGE, equalTo(userResponse.getMessage()));
    }

}
