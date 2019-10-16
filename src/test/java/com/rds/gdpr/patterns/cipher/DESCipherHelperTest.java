package com.rds.gdpr.patterns.cipher;

import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
class DESCipherHelperTest {

    private final Faker faker = new Faker();

    @Test
    public void encrypt() {
        String message = faker.lorem().paragraph(20);
        DESCipherHelper des = DESCipherHelper.getInstance();
        log.info("Message: {}", message);
        des.key(secretKey ->
                des.encrypt(message, secretKey, encrypted -> {
                    log.info("Encrypted: {}", encrypted);
                    des.decrypt(encrypted, secretKey, decypted -> {
                        log.info("Decrypted: {}", decypted);
                        Assertions.assertEquals(message, decypted);
                    });
                }));
    }

    @Test
    public void save() {
        DESCipherHelper des = DESCipherHelper.getInstance();
        des.key(from ->
                des.saveKey(from, s ->
                        des.loadKey(s, to -> {
                            log.info("from: {}", from.getEncoded());
                            log.info("to: {}", to.getEncoded());
                            Assertions.assertArrayEquals(from.getEncoded(), to.getEncoded());
                        })));
    }

}