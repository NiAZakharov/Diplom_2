package edu.practikum.dto.order;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class OrderLight {
    @EqualsAndHashCode.Exclude
    private String _id;
    private List<String> ingredients;
    private String status;

    private String name;
    @EqualsAndHashCode.Exclude
    private String createdAt;
    @EqualsAndHashCode.Exclude
    private String updatedAt;
    @EqualsAndHashCode.Exclude
    private int number;
}
