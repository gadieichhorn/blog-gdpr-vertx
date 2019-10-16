package com.rds.gdpr.patterns.cipher;

import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.util.Base64;
import java.util.function.Consumer;

@Slf4j
public abstract class CipherHelper {

    protected abstract void cipher(Consumer<Cipher> handler);

    public void encrypt(String plainText, Key key, Consumer<String> handler) {
        cipher(cipher ->
                init(cipher, Cipher.ENCRYPT_MODE, key, aVoid ->
                        doFinal(cipher, plainText.getBytes(CharsetUtil.UTF_8), encrypted ->
                                encode(encrypted, handler))));

    }

    public void decrypt(String cipherText, Key key, Consumer<String> handler) {
        cipher(cipher ->
                init(cipher, Cipher.DECRYPT_MODE, key, aVoid ->
                        decode(cipherText, decoded ->
                                doFinal(cipher, decoded, bytes -> handler.accept(new String(bytes, CharsetUtil.UTF_8))))));
    }

    public void init(Cipher cipher, int mode, Key key, Consumer<Void> handler) {
        try {
            cipher.init(mode, key);
            handler.accept(null);
        } catch (InvalidKeyException e) {
            log.error("InvalidKeyException", e);
        }
    }

    public void doFinal(Cipher cipher, byte[] bytes, Consumer<byte[]> handler) {
        try {
            handler.accept(cipher.doFinal(bytes));
        } catch (IllegalBlockSizeException e) {
            log.error("IllegalBlockSizeException", e);
        } catch (BadPaddingException e) {
            log.error("BadPaddingException", e);
        }
    }

    public void decode(String text, Consumer<byte[]> handler) {
        handler.accept(Base64.getDecoder().decode(text));
    }

    public void encode(byte[] bytes, Consumer<String> handler) {
        handler.accept(Base64.getEncoder().encodeToString(bytes));
    }

}
