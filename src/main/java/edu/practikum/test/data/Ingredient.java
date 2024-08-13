package edu.practikum.test.data;

import edu.practikum.dto.order.IngredientBody;

import java.util.List;

public class Ingredient {

    public static IngredientBody setIngredients() {
        return IngredientBody
                .builder()
                .ingredients(List.of("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f"))
                .build();
    }

    public static IngredientBody setWrongIngredients() {
        return IngredientBody
                .builder()
                .ingredients(List.of("FF61c0c5a71d1f82001bdaaa6d", "FF61c0c5a71d1f82001bdaaa6f"))
                .build();
    }

}
