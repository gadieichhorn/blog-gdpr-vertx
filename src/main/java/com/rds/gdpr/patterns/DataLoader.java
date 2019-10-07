package com.rds.gdpr.patterns;

import com.github.javafaker.Faker;
import com.rds.gdpr.patterns.model.ChatMessage;
import com.rds.gdpr.patterns.model.User;
import com.rds.gdpr.patterns.repository.UserRepository;
import com.rds.gdpr.patterns.service.ChatConsumer;
import com.rds.gdpr.patterns.service.ChatProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor

@Component
public class DataLoader {

    private final Faker faker = new Faker();

    private final UserRepository userRepository;
    private final ChatProducer chatProducer;
    private final ChatConsumer chatConsumer;

    @EventListener(ApplicationReadyEvent.class)
    public void load() {

        userRepository
                .deleteAll()
                .thenMany(Flux.fromStream(() ->
                        Stream.generate(() -> faker.name())
                                .map(name -> User.builder()
                                        .name(name.username())
                                        .email(faker.internet().emailAddress())
                                        .privateKey(faker.random().hex())
                                        .publicKey(faker.random().hex())
                                        .build())
                                .limit(10))
                        .flatMap(user -> userRepository.save(user)))
                .thenMany(userRepository.findAll())
                .subscribe(user -> log.info("User: {}", user));

        Flux.fromStream(Stream
                .generate(() -> ChatMessage.builder()
                        .time(Instant.now())
                        .user(UUID.randomUUID().toString())
                        .message(faker.lorem().paragraph())
                        .build()))
                .delayElements(Duration.ofSeconds(1))
                .subscribe(message -> chatProducer.send(message));

    }

}
