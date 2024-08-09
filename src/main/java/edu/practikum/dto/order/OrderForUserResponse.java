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
public class OrderForUserResponse {
    private boolean success;
    private List<OrderLight> orders;
    @EqualsAndHashCode.Exclude
    private Integer total;
    @EqualsAndHashCode.Exclude
    private Integer totalToday;
}