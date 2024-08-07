package edu.practikum.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {

    private boolean success;
    private User user;
    private String message;
    private String accessToken;
    private String refreshToken;

//    // преобразуем основной класс User и избавляемся от поля пароль
//    public static User fromMainUser(User mainUser) {
//        User user = new User();
//        user.setEmail(mainUser.getEmail());
//        user.setName(mainUser.getName());
//        return user;
//    }
}
