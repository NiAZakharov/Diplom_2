package user;

import com.github.javafaker.Faker;
import edu.practikum.dto.user.User;
import edu.practikum.dto.user.UserResponse;
import edu.practikum.util.BaseScenario;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Locale;
import java.util.stream.Stream;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;

@Slf4j
@DisplayName("Создание пользователя")
public class CreateUserTest extends BaseScenario {

    private final static String API_PATH = CONNECTION_PROPERTIES.getUserRegisterPath();
    private final static String FAIL_MESSAGE = "Email, password and name are required fields";
    private final static String CONFLICT_MESSAGE = "User already exists";
    private final static Faker FAKER = new Faker(new Locale("ru_Ru", "RU"));

    static Stream<User> userErrorProvider() {
        return Stream.of(
                User.builder()
                        .email(FAKER.internet().emailAddress())
                        .name(FAKER.name().username())
                        .build(),
                User.builder()
                        .email(FAKER.internet().emailAddress())
                        .password(FAKER.internet().password())
                        .build(),
                User.builder()
                        .password(FAKER.internet().password())
                        .name(FAKER.name().username())
                        .build(),
                User.builder()
                        .email(FAKER.internet().emailAddress())
                        .build(),
                User.builder()
                        .name(FAKER.name().username())
                        .build(),
                User.builder()
                        .password(FAKER.internet().password())
                        .build(),
                User.builder().build()
        );
    }

    @Test
    @DisplayName("Успешное создание пользователя")
    @Description("Создание пользователя и проверка ответа метода")
    public void createUniqUserTest() {

        //Тут использую 2 объекта, потому что в первом есть нужный статус код
        //А второй позволяет не использовать jsonPath и искать по конкретным ключам
        Response response = createUniqueUser();
        UserResponse userResponse = response.getBody().as(UserResponse.class);

        //проверили статус код
        assertThat("Статус код не соответствует ожидаемому", SC_OK, equalTo(response.getStatusCode()));
        //проверили тело ответа
        assertThat("Флаг пришел false", true, equalTo(userResponse.isSuccess()));
        assertThat("Токен не получен", userResponse.getAccessToken(), not(blankOrNullString()));
        assertThat("Полученный токен не валиден", userResponse.getAccessToken(), startsWith("Bearer"));
    }

    @Test
    @DisplayName("Попытка создать существующего пользователя")
    @Description("Попытка создать существующего пользователя")
    public void createDopplerUserTest() {

        //Тут создали нового пользователя и положили ответ сразу в объект, чтобы потом создать с такими же параметрами
        UserResponse userResponse = createUniqueUser().getBody().as(UserResponse.class);

        User newUser = User
                .builder()
                .email(userResponse.getUser().getEmail())
                .password(FAKER.internet().password())
                .name(userResponse.getUser().getName())
                .build();

        Response response = sendPostRequest("Вызов метода создания пользователя", newUser, API_PATH);

        checkEasyResponse(response, SC_FORBIDDEN, "success", "message", false,
                CONFLICT_MESSAGE, "Проверка ответа при создании дубля");
    }

    @ParameterizedTest(name = "Создание пользователя с одним из отсутствующих параметров {index}")
    @DisplayName("Не успешное создание пользователя")
    @Description("Создание пользователя без пароля")
    @MethodSource("userErrorProvider")
    public void createWithoutPasswordUserTest(User user) {

        Response response = sendPostRequest("Вызов метода создания пользователя", user, API_PATH);

        checkEasyResponse(response, SC_FORBIDDEN, "message",
                FAIL_MESSAGE, "Проверка негативного ответа");
    }


}
