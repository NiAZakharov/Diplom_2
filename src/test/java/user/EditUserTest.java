package user;

import com.github.javafaker.Faker;
import edu.practikum.dto.user.User;
import edu.practikum.dto.user.UserResponse;
import edu.practikum.util.BaseScenario;
import io.qameta.allure.Description;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Locale;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Slf4j
@DisplayName("Редактирование пользователя")
public class EditUserTest extends BaseScenario {

    //    private final static String API_PATH = "/auth/user"; //PATCH
    private final static String API_PATH = CONNECTION_PROPERTIES.getUserGetPatchPath();
    private final static Faker FAKER = new Faker(new Locale("ru_Ru", "RU"));

    private final static String AUTH_HEADER_NAME = "Authorization";
    private final static String EXP_MESSAGE = "jwt expired";
    private final static String NON_AUTHORIZED_MESSAGE = "You should be authorised";

    private final static String EXPIRE_TOKEN = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9." +
            "eyJpZCI6IjY2YjJhYTg1OWVkMjgwMDAxYjQ4NjFhMiIsImlhdCI6MTcyMjk4NTA5MywiZXhwIjoxNzIyOTg2MjkzfQ." +
            "JqmB9TkYxaxqx4X-2NrEvVGU8aSTrhxIIo6B9ms1w68";

    private String token;

    /*
    Тут указан набор данных которые будут подставляться в PATCH

    На последних 2-х наборах тест упадет и отдаст 403 вместо 200
    Считаю это корректным, т.к. мы можем полностью стереть свои данные если не указано иное
    (судя по форме на сайте - можем). А принадлежность к конкретному пользователю определяется по текущему токену
    такая реализация не по REST (Нужен id пользователя) но метод его попросту не отдает
    Т.е. получается что поле email тут обязательное (что не указано в документации) и тест это нашел
     */
    static Stream<User> userErrorProvider() {
        return Stream.of(
                User.builder()
                        .email(FAKER.internet().emailAddress())
                        .password(FAKER.internet().password())
                        .name(FAKER.name().username())
                        .build(),
                User.builder()
                        .email(FAKER.internet().emailAddress())
                        .name(FAKER.internet().password())
                        .password("")
                        .build(),
                User.builder()
                        .email(FAKER.internet().emailAddress())
                        .name("")
                        .password("")
                        .build(),
                User.builder()
                        .email("")
                        .name(FAKER.internet().password())
                        .password("")
                        .build(),
                User.builder()
                        .email("")
                        .name("")
                        .password("")
                        .build()
        );
    }

    @BeforeEach
    public void getActiveToken() {
        //Создадим стандартного пользователя и сразу получим его токен. Т.к. больше этот user в тесте не нужен
        token = createUniqueUser().getHeader("Authorization");
    }

    @ParameterizedTest(name = "Редактирование пользователя {index}")
    @DisplayName("Редактирование пользователя")
    @Description("Редактирование пользователя с учетом авторизации")
    @MethodSource("userErrorProvider")
    public void editUserWithTokenTest(User patchUser) {

        UserResponse userResponse = sendPathUserRequest("Отправка PATH Запроса с токеном",
                AUTH_HEADER_NAME, token, patchUser, API_PATH, SC_OK);

        //Тут проверяем что в ответе метода вернулось то, что мы передали
        assertThat(true, equalTo(userResponse.isSuccess()));
        assertThat(patchUser.getEmail(), equalTo(userResponse.getUser().getEmail()));
        assertThat(patchUser.getName(), equalTo(userResponse.getUser().getName()));

    }

    @ParameterizedTest(name = "Редактирование пользователя без авторизации {index}")
    @DisplayName("Редактирование пользователя")
    @Description("Редактирование пользователя без авторизации")
    @MethodSource("userErrorProvider")
    public void editUserWithoutTokenTest(User patchUser) {

        UserResponse userResponse = sendPathUserRequest("Отправка PATH Запроса без токена",
                AUTH_HEADER_NAME, "", patchUser, API_PATH, SC_UNAUTHORIZED);

        //Тут проверяем что в ответе метода вернулось то, что мы передали
        assertThat(false, equalTo(userResponse.isSuccess()));
        assertThat(NON_AUTHORIZED_MESSAGE, equalTo(userResponse.getMessage()));
    }

    @ParameterizedTest(name = "Редактирование пользователя с истекшим токеном {index}")
    @DisplayName("Редактирование пользователя")
    @Description("Редактирование пользователя без авторизации")
    @MethodSource("userErrorProvider")
    public void editUserWithExpTokenTest(User patchUser) {

        UserResponse userResponse = sendPathUserRequest("Отправка PATH Запроса с просроченным токеном",
                AUTH_HEADER_NAME, EXPIRE_TOKEN, patchUser, API_PATH, SC_FORBIDDEN);

        //Тут проверяем что в ответе метода вернулось то, что мы передали
        assertThat(false, equalTo(userResponse.isSuccess()));
        assertThat(EXP_MESSAGE, equalTo(userResponse.getMessage()));
    }

}
