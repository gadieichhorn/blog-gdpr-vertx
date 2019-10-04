package com.rds.gdpr.patterns;

import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@ToString
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Integer version;

    @NonNull
    private String name;

    @NonNull
    private String email;

    @NonNull
    private String publicKey;

    @NonNull
    private String privateKey;

    @Builder
    public User(Long id, Integer version, @NonNull String name, @NonNull String email, @NonNull String publicKey, @NonNull String privateKey) {
        this.id = id;
        this.version = version;
        this.name = name;
        this.email = email;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

}
