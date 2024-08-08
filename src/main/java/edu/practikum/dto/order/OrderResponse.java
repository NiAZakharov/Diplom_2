package edu.practikum.dto.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.practikum.dto.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderResponse {
    private Ingredient[] ingredients;
    private String _id;
    private User owner;
    private String status;
    private String name;
    private String createAt;
    private String updateAt;
    private int number;
    private int price;
}
