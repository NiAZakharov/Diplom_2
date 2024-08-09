package edu.practikum.test.data;

import edu.practikum.dto.order.Ingredient;
import edu.practikum.dto.order.Order;
import edu.practikum.dto.order.OrderForUserResponse;
import edu.practikum.dto.order.OrderLight;
import edu.practikum.dto.order.OrderResponse;
import edu.practikum.dto.user.User;
import edu.practikum.dto.user.UserResponse;

import java.util.Arrays;

public class OrderAnswer {

    public static OrderResponse getExpectedApiResponse(UserResponse userResponse) {
        return OrderResponse.builder()
                .success(true)
                .name("Бессмертный флюоресцентный бургер")
                .order(Order.builder()
                        .ingredients(Arrays.asList(
                                edu.practikum.dto.order.Ingredient.builder()
                                        ._id("61c0c5a71d1f82001bdaaa6d")
                                        .name("Флюоресцентная булка R2-D3")
                                        .type("bun")
                                        .proteins(44)
                                        .fat(26)
                                        .carbohydrates(85)
                                        .calories(643)
                                        .price(988)
                                        .image("https://code.s3.yandex.net/react/code/bun-01.png")
                                        .image_mobile("https://code.s3.yandex.net/react/code/bun-01-mobile.png")
                                        .image_large("https://code.s3.yandex.net/react/code/bun-01-large.png")
                                        .__v(0)
                                        .build(),
                                Ingredient.builder()
                                        ._id("61c0c5a71d1f82001bdaaa6f")
                                        .name("Мясо бессмертных моллюсков Protostomia")
                                        .type("main")
                                        .proteins(433)
                                        .fat(244)
                                        .carbohydrates(33)
                                        .calories(420)
                                        .price(1337)
                                        .image("https://code.s3.yandex.net/react/code/meat-02.png")
                                        .image_mobile("https://code.s3.yandex.net/react/code/meat-02-mobile.png")
                                        .image_large("https://code.s3.yandex.net/react/code/meat-02-large.png")
                                        .__v(0)
                                        .build()
                        ))
                        ._id("66b42c1d9ed280001b4871f2")
                        .owner(User.builder()
                                .name(userResponse.getUser().getName())
                                .email(userResponse.getUser().getEmail())
                                .createdAt("2024-08-08T02:23:25.265Z")
                                .updatedAt("2024-08-08T02:23:25.265Z")
                                .build())
                        .status("done")
                        .name("Бессмертный флюоресцентный бургер")
                        .createdAt("2024-08-08T02:23:25.722Z")
                        .updatedAt("2024-08-08T02:23:26.501Z")
                        .number(104046)
                        .price(2325)
                        .build())
                .build();
    }

    public static OrderResponse getExpectedApiResponseWithoutToken() {
        return OrderResponse.builder()
                .success(true)
                .name("Бессмертный флюоресцентный бургер")
                .order(Order.builder()
                        .number(104046)
                        .build())
                .build();
    }

    public static OrderForUserResponse getOrderForAuthorizedUser() {

        return OrderForUserResponse.builder()
                .success(true)
                .orders(Arrays.asList(
                        OrderLight.builder()
                                ._id("66b60c3e9ed280001b4885a3")
                                .status("done")
                                .name("Бессмертный флюоресцентный бургер")
                                .createdAt("2024-08-09T12:31:58.252Z")
                                .updatedAt("2024-08-09T12:31:59.037Z")
                                .number(104407)
                                .ingredients(Arrays.asList(
                                        "61c0c5a71d1f82001bdaaa6d",
                                        "61c0c5a71d1f82001bdaaa6f"
                                ))
                                .build()
                ))
                .total(104407)
                .totalToday(338)
                .build();
    }
}
