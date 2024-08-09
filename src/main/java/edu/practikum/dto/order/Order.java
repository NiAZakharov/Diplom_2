package edu.practikum.dto.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.practikum.dto.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Order {
    private List<Ingredient> ingredients;
    @EqualsAndHashCode.Exclude
    private String _id;
    private User owner;
    private String status;
    private String name;
    @EqualsAndHashCode.Exclude
    private String createdAt;
    @EqualsAndHashCode.Exclude
    private String updatedAt;
    @EqualsAndHashCode.Exclude
    private int number;
    private int price;
}
