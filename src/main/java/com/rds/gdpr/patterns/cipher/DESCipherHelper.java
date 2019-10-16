package com.rds.gdpr.patterns.cipher;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.function.Consumer;

@Slf4j
public class DESCipherHelper extends CipherHelper {

    @Getter
    private static final DESCipherHelper instance = new DESCipherHelper();

    private KeyGenerator keyGenerator;

    public DESCipherHelper() {
        try {
            keyGenerator = KeyGenerator.getInstance("DES");
        } catch (NoSuchAlgorithmException e) {
            log.error("NoSuchAlgorithmException", e);
        }
    }

    public void key(Consumer<SecretKey> handler) {
        handler.accept(keyGenerator.generateKey());
    }

    public void loadKey(String key, Consumer<SecretKey> handler) {
        handler.accept(new SecretKeySpec(Base64.getDecoder().decode(key), "DES"));
    }

    public void saveKey(SecretKey key, Consumer<String> handler) {
        handler.accept(Base64.getEncoder().encodeToString(key.getEncoded()));
    }

    @Override
    public void cipher(Consumer<Cipher> handler) {
        try {
            handler.accept(Cipher.getInstance("DES/ECB/PKCS5Padding"));
        } catch (NoSuchAlgorithmException e) {
            log.error("NoSuchAlgorithmException", e);
        } catch (NoSuchPaddingException e) {
            log.error("NoSuchPaddingException", e);
        }
    }

}
