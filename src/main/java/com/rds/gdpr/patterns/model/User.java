package com.rds.gdpr.patterns.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.netty.util.CharsetUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Data
@Slf4j
@ToString
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    public static final String COLLECTION = "users";

    private static KeyPairGenerator keyGen;

    private static KeyFactory kf;

    static {
        try {
            kf = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            log.error("NoSuchAlgorithmException", e);
        }
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
    private String privateKey;

    @JsonProperty("publicKey")
    private String publicKey;

    @Builder
    public User(String id, @NonNull String name) {
        this.id = id;
        this.name = name;
        KeyPair pair = keyGen.generateKeyPair();
        this.publicKey = Base64.getEncoder().encodeToString(pair.getPublic().getEncoded());
        this.privateKey = Base64.getEncoder().encodeToString(pair.getPrivate().getEncoded());
    }

    public String encrypt(String message) throws Exception {
        return encrypt(message, kf.generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey))));
    }

    public String decrypt(String message) throws Exception {
        return decrypt(message, kf.generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(this.privateKey))));
    }

    public static String encrypt(String plainText, PublicKey publicKey) throws Exception {
        Cipher encryptCipher = Cipher.getInstance("RSA");
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] cipherText = encryptCipher.doFinal(plainText.getBytes(CharsetUtil.UTF_8));
        return Base64.getEncoder().encodeToString(cipherText);
    }

    public static String decrypt(String cipherText, PrivateKey privateKey) throws Exception {
        byte[] bytes = Base64.getDecoder().decode(cipherText);
        Cipher decriptCipher = Cipher.getInstance("RSA");
        decriptCipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(decriptCipher.doFinal(bytes), CharsetUtil.UTF_8);
    }

}
