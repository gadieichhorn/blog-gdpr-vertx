package com.rds.gdpr.patterns.model;

import com.github.javafaker.Faker;
import com.rds.gdpr.patterns.cipher.RSACipherHelper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
class UserTest {

    private final Faker faker = new Faker();

    @Test
    public void encrypt() throws Exception {
        User user = User.builder().name(faker.name().username()).build();
        String message = faker.random().hex(117);
        log.debug("Message: [{}] {}", message.getBytes().length, message);
        RSACipherHelper rsa = RSACipherHelper.getInstance();
        rsa.loadPublicKey(user.getPublicKey(), publicKey ->
                rsa.encrypt(message, publicKey, encrypted -> {
                    log.info("Encrypted: {} {}", message, encrypted);
                    rsa.loadPrivateKey(user.getPrivateKey(), privateKey ->
                            rsa.decrypt(encrypted, privateKey, decrypted -> {
                                log.info("Decrypted: {} ", decrypted);
                                Assertions.assertEquals(decrypted, message);
                            }));
                }));
    }

}