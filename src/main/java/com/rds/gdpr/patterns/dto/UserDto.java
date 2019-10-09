package com.rds.gdpr.patterns.dto;

import lombok.*;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String id;

    private String name;

}
