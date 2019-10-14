package com.rds.gdpr.patterns.model;

import com.github.javafaker.Faker;
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
        String encrypted = user.encrypt(message);
        log.info("Encrypted: {} {}", message, encrypted);
        String results = user.decrypt(encrypted);
        log.info("Decrypted: {} ", results);
        Assertions.assertEquals(results, message);
    }

}