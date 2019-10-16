package com.rds.gdpr.patterns.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rds.gdpr.patterns.cipher.RSACipherHelper;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Base64;

@Data
@Slf4j
@ToString
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    @JsonProperty("_id")
    private String id;

    @NonNull
    @JsonProperty("name")
    private String name;

    @JsonProperty("privateKey")
    private String privateKey;

    @JsonProperty("publicKey")
    private String publicKey;

    @Builder
    public User(String id, @NonNull String name) {
        this.id = id;
        this.name = name;
        RSACipherHelper.pair(pair -> {
            this.publicKey = Base64.getEncoder().encodeToString(pair.getPublic().getEncoded());
            this.privateKey = Base64.getEncoder().encodeToString(pair.getPrivate().getEncoded());
        });
    }

}
