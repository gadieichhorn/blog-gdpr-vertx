package com.rds.gdpr.patterns.cipher;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.function.Consumer;

@Slf4j
public class RSACipherHelper extends CipherHelper {

    @Getter
    private static final RSACipherHelper instance = new RSACipherHelper();

    private KeyPairGenerator keyGen;

    private KeyFactory kf;

    private RSACipherHelper() {
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

    public void pair(Consumer<KeyPair> handler) {
        handler.accept(keyGen.generateKeyPair());
    }

    public void saveKey(Key key, Consumer<String> handler) {
        handler.accept(Base64.getEncoder().encodeToString(key.getEncoded()));
    }

    public void loadPublicKey(String key, Consumer<PublicKey> handler) {
        try {
            handler.accept(kf.generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(key))));
        } catch (InvalidKeySpecException e) {
            log.error("InvalidKeySpecException", e);
        }
    }

    public void loadPrivateKey(String key, Consumer<PrivateKey> handler) {
        try {
            handler.accept(kf.generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(key))));
        } catch (InvalidKeySpecException e) {
            log.error("InvalidKeySpecException", e);
        }
    }

    @Override
    public void cipher(Consumer<Cipher> handler) {
        try {
            handler.accept(Cipher.getInstance("RSA"));
        } catch (NoSuchAlgorithmException e) {
            log.error("NoSuchAlgorithmException", e);
        } catch (NoSuchPaddingException e) {
            log.error("NoSuchPaddingException", e);
        }
    }

}
