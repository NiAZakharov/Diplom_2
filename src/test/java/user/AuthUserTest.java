package user;

import edu.practikum.dto.user.User;
import edu.practikum.dto.user.UserResponse;
import edu.practikum.util.BaseScenario;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class AuthUserTest extends BaseScenario {

    private final static String API_PATH = CONNECTION_PROPERTIES.getUserLoginPath();

    @Test
    public void userCanLoginTest() {

//        Response response = createUniqueUser();
        UserResponse userResponse = createUniqueUser().getBody().as(UserResponse.class);

        User newUser = User
                .builder()
                .email(userResponse.getUser().getEmail())
                .password(userResponse.getUser().getPassword())//надо придумать откуда брать пароль
                .build();

        Response response = sendPostRequest("Авторизация существующим пользователем", newUser, API_PATH);
//проверить что код ответа 200
        //в ответе есть токен
        //ответ соответствует структуре UserResponse

    }


    @Test
    public void userCanNotLoginTest() {


        //должно вернуться 401
        /*
        {
            "success": false,
            "message": "email or password are incorrect"
        }
         */
    }


}
