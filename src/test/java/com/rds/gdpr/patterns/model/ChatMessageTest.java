package com.rds.gdpr.patterns.model;

import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.UUID;

@Slf4j
class ChatMessageTest {

    private final Faker faker = new Faker();

    @Test
    public void encrypt() throws Exception {
        String text = faker.lorem().paragraph(10);
        ChatMessage message = ChatMessage.builder()
                .from(UUID.randomUUID().toString())
                .message(text)
                .build();
    }

}