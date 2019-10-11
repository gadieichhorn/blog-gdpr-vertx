package com.rds.gdpr.patterns.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

@Data
@Slf4j
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    public static final String COLLECTION = "users";

    private static KeyPairGenerator keyGen;

    static {
        try {
            keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(1024);
        } catch (NoSuchAlgorithmException e) {
            log.error("NoSuchAlgorithmException", e);
        }
    }

    @JsonProperty("_id")
    private String id;

    @NonNull
    @JsonProperty("name")
    private String name;

    @JsonProperty("privateKey")
    private Binary privateKey;

    @JsonProperty("publicKey")
    private Binary publicKey;

    @Builder
    public User(String id, @NonNull String name) {
        this.id = id;
        this.name = name;
        KeyPair pair = keyGen.generateKeyPair();
        this.publicKey = Binary.builder().data(pair.getPublic().getEncoded()).build();
        this.privateKey = Binary.builder().data(pair.getPrivate().getEncoded()).build();
    }

}
