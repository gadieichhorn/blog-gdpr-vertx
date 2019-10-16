package com.rds.gdpr.patterns.cipher;

import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.function.Consumer;

@Slf4j
public class RSACipherHelper {

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

    public static void pair(Consumer<KeyPair> handler) {
        handler.accept(keyGen.generateKeyPair());
    }

    public static void loadPublicKey(String key, Consumer<PublicKey> handler) throws InvalidKeySpecException {
        handler.accept(kf.generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(key))));
    }

    public static void loadPrivateKey(String key, Consumer<PrivateKey> handler) {
        try {
            handler.accept(kf.generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(key))));
        } catch (InvalidKeySpecException e) {
            log.error("InvalidKeySpecException", e);
        }
    }

    public static void encrypt(String plainText, PublicKey publicKey, Consumer<String> handler) {
        cipher(cipher ->
                init(cipher, Cipher.ENCRYPT_MODE, publicKey, aVoid ->
                        doFinal(cipher, plainText.getBytes(CharsetUtil.UTF_8), encrypted ->
                                encode(encrypted, handler))));

    }

    public static void decrypt(String cipherText, PrivateKey privateKey, Consumer<String> handler) {
        cipher(cipher ->
                init(cipher, Cipher.DECRYPT_MODE, privateKey, aVoid ->
                        decode(cipherText, decoded ->
                                doFinal(cipher, decoded, bytes -> handler.accept(new String(bytes, CharsetUtil.UTF_8))))));
    }

    private static void cipher(Consumer<Cipher> handler) {
        try {
            handler.accept(Cipher.getInstance("RSA"));
        } catch (NoSuchAlgorithmException e) {
            log.error("NoSuchAlgorithmException", e);
        } catch (NoSuchPaddingException e) {
            log.error("NoSuchPaddingException", e);
        }
    }

    private static void init(Cipher cipher, int mode, Key key, Consumer<Void> handler) {
        try {
            cipher.init(mode, key);
            handler.accept(null);
        } catch (InvalidKeyException e) {
            log.error("InvalidKeyException", e);
        }
    }

    private static void doFinal(Cipher cipher, byte[] bytes, Consumer<byte[]> handler) {
        try {
            handler.accept(cipher.doFinal(bytes));
        } catch (IllegalBlockSizeException e) {
            log.error("IllegalBlockSizeException", e);
        } catch (BadPaddingException e) {
            log.error("BadPaddingException", e);
        }
    }

    private static void decode(String text, Consumer<byte[]> handler) {
        handler.accept(Base64.getDecoder().decode(text));
    }

    private static void encode(byte[] bytes, Consumer<String> handler) {
        handler.accept(Base64.getEncoder().encodeToString(bytes));
    }

}
