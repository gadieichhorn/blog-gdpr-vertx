package com.rds.gdpr.patterns.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.rds.gdpr.patterns.model.User;
import lombok.*;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    private String id;

    private String name;

    public static UserDto of(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

}
