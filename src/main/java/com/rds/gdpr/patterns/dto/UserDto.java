package com.rds.gdpr.patterns.dto;

import com.rds.gdpr.patterns.model.User;
import lombok.*;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
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
