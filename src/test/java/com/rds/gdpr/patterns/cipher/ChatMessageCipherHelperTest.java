package com.rds.gdpr.patterns.cipher;

import com.github.javafaker.Faker;
import com.rds.gdpr.patterns.dto.ChatMessageDto;
import com.rds.gdpr.patterns.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.UUID;

@Slf4j
class ChatMessageCipherHelperTest {

    private final Faker faker = new Faker();

    @Test
    public void encrypt() {
        User user = User.builder()
                .id(UUID.randomUUID().toString())
                .name(faker.name().username())
                .build();

        ChatMessageDto dto = ChatMessageDto.builder()
                .from(user.getId())
                .message(faker.lorem().paragraph())
                .build();

        ChatMessageCipherHelper.getInstance().encrypt(user, dto, message -> {
            log.info("Message: {}", message);
        });
    }

    @Test
    public void decrypt() {
        User user = User.builder()
                .id(UUID.randomUUID().toString())
                .name(faker.name().username())
                .build();

        ChatMessageDto dto = ChatMessageDto.builder()
                .from(user.getId())
                .message(faker.lorem().paragraph())
                .build();

        ChatMessageCipherHelper.getInstance().encrypt(user, dto, message -> {
            ChatMessageCipherHelper.getInstance().decrypt(user, message, chatMessageDto -> {
                log.info("DTO: {}", chatMessageDto);
            });
        });

    }

}