package edu.practikum.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    private String email;
    private String password;
    private String name;
    @EqualsAndHashCode.Exclude
    private String createdAt;
    @EqualsAndHashCode.Exclude
    private String updatedAt;
}
