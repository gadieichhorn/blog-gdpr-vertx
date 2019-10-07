package com.rds.gdpr.patterns.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Data
@NoArgsConstructor
@ToString

@Document
public class User {

    @Id
    private String id;

    @NonNull
    private String name;

    @NonNull
    private String email;

    @NonNull
    private String publicKey;

    @NonNull
    private String privateKey;

    @Builder
    public User(String id, @NonNull String name, @NonNull String email, @NonNull String publicKey, @NonNull String privateKey) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }
}
