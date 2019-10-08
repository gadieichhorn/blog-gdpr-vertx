package com.rds.gdpr.patterns.service;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class User {

    private String id;
    private String name;

    @Builder
    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
