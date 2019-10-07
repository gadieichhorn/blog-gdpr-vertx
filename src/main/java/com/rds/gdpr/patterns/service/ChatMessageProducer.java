package com.rds.gdpr.patterns.service;

import com.github.javafaker.Faker;
import com.rds.gdpr.patterns.model.ChatMessage;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.Stream;

@Component
public class ChatMessageProducer {

    private final Faker faker = new Faker();

    public Flux<ChatMessage> messages(String id) {
        return Flux.fromStream(Stream.generate(() -> ChatMessage.builder()
                .time(Instant.now())
                .user(id)
                .message(faker.lorem().paragraph())
                .build()))
                .delayElements(Duration.ofSeconds(1));
    }

}
