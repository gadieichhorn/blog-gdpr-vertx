package com.rds.gdpr.patterns.repository;

import com.github.javafaker.Faker;
import com.rds.gdpr.patterns.AbstractMongoTest;
import com.rds.gdpr.patterns.model.User;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxTestContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

import java.util.UUID;

@Slf4j
class UsersRepositoryTest extends AbstractMongoTest {

    private UsersRepository usersRepository;
    private final Faker faker = new Faker();

    @BeforeEach
    public void beforeEach() {
        usersRepository = new UsersRepository(mongoClient);
    }

    @RepeatedTest(3)
    public void canGetAllUsers(Vertx vertx, VertxTestContext testContext) {
        usersRepository.findAll(testContext.succeeding(all -> testContext.verify(() -> {
            log.info("All: {}", all);
            Assertions.assertNotNull(all);
            testContext.completeNow();
        })));
    }

    @RepeatedTest(3)
    public void canSaveUser(VertxTestContext testContext) {
        usersRepository.save(User.builder().name(faker.name().username()).build(), testContext.succeeding(save -> testContext.verify(() -> {
            log.info("Save: {}", save);
            Assertions.assertFalse(save.isEmpty());
            testContext.completeNow();
        })));
    }

    @RepeatedTest(3)
    public void canDeleteUser(VertxTestContext testContext) {
        usersRepository.save(User.builder().name(faker.name().username()).build(), testContext.succeeding(save ->
                usersRepository.delete(save, testContext.succeeding(delete -> testContext.verify(() -> {
                    Assertions.assertEquals(1, delete);
                    testContext.completeNow();
                })))));
    }

    @RepeatedTest(3)
    public void failedDeleteUser(VertxTestContext testContext) {
        usersRepository.delete(UUID.randomUUID().toString(), testContext.succeeding(delete -> testContext.verify(() -> {
            Assertions.assertEquals(0, delete);
            testContext.completeNow();
        })));
    }

    @RepeatedTest(3)
    public void failedSaveUserWithMissingName(VertxTestContext testContext) {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                usersRepository.save(User.builder().build(), testContext.succeeding(save -> testContext.verify(() -> {
                }))));
        testContext.completeNow();
    }

    @RepeatedTest(3)
    public void canSaveAndGetUser(VertxTestContext testContext) {
        usersRepository.save(User.builder().name(faker.name().username()).build(), testContext.succeeding(save -> {
            log.info("Save: {}", save);
            Assertions.assertFalse(save.isEmpty());
            usersRepository.findById(save, testContext.succeeding(user -> testContext.verify(() -> {
                user.ifPresent(model -> {
                    log.info("User: {}", model);
                    Assertions.assertEquals(save, model.getId());
                    testContext.completeNow();
                });
            })));
        }));
    }

    @RepeatedTest(3)
    public void failToGetWrongUserById(VertxTestContext testContext) {
        usersRepository.findById(UUID.randomUUID().toString(), testContext.succeeding(user -> testContext.verify(() -> {
            Assertions.assertFalse(user.isPresent());
            testContext.completeNow();
        })));
    }

}