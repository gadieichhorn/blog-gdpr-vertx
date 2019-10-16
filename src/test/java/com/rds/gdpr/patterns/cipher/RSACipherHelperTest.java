package com.rds.gdpr.patterns.cipher;

import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
class RSACipherHelperTest {

    private final Faker faker = new Faker();

    @Test
    public void encrypt() throws Exception {
        String message = faker.random().hex(117);
        log.debug("Message: [{}] {}", message.getBytes().length, message);
        RSACipherHelper rsa = RSACipherHelper.getInstance();
        rsa.pair(keyPair ->
                rsa.encrypt(message, keyPair.getPublic(), encrypted -> {
                    log.info("Encrypted: {} {}", message, encrypted);
                    rsa.decrypt(encrypted, keyPair.getPrivate(), decrypted -> {
                        log.info("Decrypted: {} ", decrypted);
                        Assertions.assertEquals(decrypted, message);
                    });
                }));
    }

    @Test
    public void saveAndLoadPublickKey() throws Exception {
        RSACipherHelper rsa = RSACipherHelper.getInstance();
        rsa.pair(keyPair ->
                rsa.saveKey(keyPair.getPublic(), key ->
                        rsa.loadPublicKey(key, publicKey -> {
                            log.info("from: {}", keyPair.getPublic().getEncoded());
                            log.info("to: {}", publicKey.getEncoded());
                            Assertions.assertArrayEquals(keyPair.getPublic().getEncoded(), publicKey.getEncoded());
                        })));
    }

}