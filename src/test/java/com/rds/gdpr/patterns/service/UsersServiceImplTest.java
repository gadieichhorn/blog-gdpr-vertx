package com.rds.gdpr.patterns.service;

import com.github.javafaker.Faker;
import com.rds.gdpr.patterns.model.User;
import com.rds.gdpr.patterns.repository.UsersRepository;
import com.rds.gdpr.patterns.repository.UsersRepositoryStab;
import io.vertx.junit5.VertxExtension;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.UUID;

@Slf4j
@ExtendWith(VertxExtension.class)
class UsersServiceImplTest {

    private final Faker faker = new Faker();

    private User user;
    private UsersRepository usersRepository;

    @BeforeEach
    public void beforeEach() {
        user = User.builder().id(UUID.randomUUID().toString()).name(faker.name().username()).build();
        usersRepository = new UsersRepositoryStab(Arrays.asList(user));
    }

//    @Test
//    public void createUserMessage(Vertx vertx, VertxTestContext testContext) {
//
//        vertx.eventBus().consumer("chat-service-inbound").handler(event -> {
//            log.info("Event: {}", event.body());
//            testContext.completeNow();
//        });
//
//        UsersService instance = new UsersServiceImpl(usersRepository, vertx.eventBus());
//        instance.createUserMessage(user.getId(), faker.lorem().paragraph(), new OperationRequest(), event -> {
//            log.info("Event: {}", event.result());
//        });
//    }

}