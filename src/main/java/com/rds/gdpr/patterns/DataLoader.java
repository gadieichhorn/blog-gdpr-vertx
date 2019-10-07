package com.rds.gdpr.patterns;

import com.github.javafaker.Faker;
import com.rds.gdpr.patterns.model.User;
import com.rds.gdpr.patterns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader {

    private final Faker faker = new Faker();
    private final UserRepository userRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void load() {

        userRepository
                .deleteAll()
                .thenMany(Flux.fromStream(() ->
                        Stream.generate(() -> faker.name())
                                .map(name -> User.builder()
                                        .name(name.username())
                                        .email(faker.internet().emailAddress())
                                        .build())
                                .limit(10))
                        .flatMap(user -> userRepository.save(user)))
                .thenMany(userRepository.findAll())
                .subscribe(user -> log.info("User: {}", user));
    }

}
