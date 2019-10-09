package com.rds.gdpr.patterns.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rds.gdpr.patterns.dto.UserDto;
import io.vertx.core.json.JsonObject;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

@Data
@Slf4j
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

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

    @JsonProperty("name")
    private String name;

    @JsonProperty("privateKey")
    private Binary privateKey;

    @JsonProperty("publicKey")
    private Binary publicKey;

    public static JsonObject of(UserDto dto) {
        KeyPair pair = keyGen.generateKeyPair();
        return new JsonObject()
                .put("name", dto.getName())
                .put("publicKey", new JsonObject().put("$binary", pair.getPublic().getEncoded()))
                .put("privateKey", new JsonObject().put("$binary", pair.getPrivate().getEncoded()));
    }

}
