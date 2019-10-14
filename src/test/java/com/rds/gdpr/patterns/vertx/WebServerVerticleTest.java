package com.rds.gdpr.patterns.vertx;

import com.github.javafaker.Faker;
import com.rds.gdpr.patterns.AbstractMongoTest;
import com.rds.gdpr.patterns.dto.UserDto;
import com.rds.gdpr.patterns.repository.UsersRepository;
import com.rds.gdpr.patterns.service.UsersService;
import com.rds.gdpr.patterns.service.UsersServiceImpl;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.ext.web.codec.BodyCodec;
import io.vertx.junit5.VertxTestContext;
import io.vertx.serviceproxy.ServiceBinder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.UUID;

@Slf4j
public class WebServerVerticleTest extends AbstractMongoTest {

    private final Faker faker = new Faker();

    private UsersRepository usersRepository;
    private ServiceBinder serviceBinder;
    private MessageConsumer<JsonObject> consumer;

    private WebClient client;

    @BeforeEach
    void beforeEach(Vertx vertx, VertxTestContext testContext) {
        usersRepository = new UsersRepository(mongoClient);
        serviceBinder = new ServiceBinder(vertx);
        consumer = serviceBinder
                .setAddress(UsersService.ADDRESS)
                .register(UsersService.class, new UsersServiceImpl(usersRepository));

        vertx.deployVerticle(new WebServerVerticle(), testContext.succeeding(id -> testContext.completeNow()));

        client = WebClient.create(vertx, new WebClientOptions()
                .setDefaultPort(8080)
                .setDefaultHost("localhost"));
    }

    @Test
    void deployed(VertxTestContext testContext) throws Throwable {
        testContext.completeNow();
    }

    @RepeatedTest(3)
    void postUserAndGetIt(VertxTestContext testContext) {
        client.post("/api/users")
                .putHeader("content-type", "application/json")
                .sendJsonObject(JsonObject.mapFrom(UserDto.builder().id("1").name(faker.name().username()).build()),
                        testContext.succeeding(post -> testContext.verify(() -> {
                            log.info("Post: {}", post.bodyAsString());
                            Assertions.assertEquals(201, post.statusCode());
                            client.get("/api/users/" + post.bodyAsString())
                                    .as(BodyCodec.jsonObject())
                                    .send(testContext.succeeding(response -> testContext.verify(() -> {
                                        Assertions.assertTrue(response.body().size() > 0);
                                        testContext.completeNow();
                                    })));
                        })));
    }

    @RepeatedTest(3)
    void postUserAndDeleteIt(VertxTestContext testContext) {
        client.post("/api/users")
                .putHeader("content-type", "application/json")
                .sendJsonObject(JsonObject.mapFrom(UserDto.builder().name(faker.name().username()).build()),
                        testContext.succeeding(post -> testContext.verify(() -> {
                            log.info("Post: {}", post.bodyAsString());
                            Assertions.assertEquals(201, post.statusCode());
                            client.delete("/api/users/" + post.bodyAsString())
                                    .send(testContext.succeeding(delete -> testContext.verify(() -> {
                                        Assertions.assertEquals(204, delete.statusCode());
                                        testContext.completeNow();
                                    })));
                        })));
    }

    @RepeatedTest(3)
    void getOneUser(VertxTestContext testContext) {
        client.get("/api/users/123123123")
                .as(BodyCodec.string())
                .send(testContext.succeeding(response -> testContext.verify(() -> {
                    Assertions.assertEquals(404, response.statusCode());
                    testContext.completeNow();
                })));
    }

    @RepeatedTest(3)
    void getAllUsers(VertxTestContext testContext) {
        client.get("/api/users")
                .as(BodyCodec.jsonArray())
                .send(testContext.succeeding(response -> testContext.verify(() -> {
                    Assertions.assertTrue(response.body().size() > 0);
                    testContext.completeNow();
                })));
    }

    @RepeatedTest(3)
    void deleteWrongUser(VertxTestContext testContext) {
        client.delete("/api/users/" + UUID.randomUUID().toString())
                .send(testContext.succeeding(delete -> testContext.verify(() -> {
                    Assertions.assertEquals(404, delete.statusCode());
                    testContext.completeNow();
                })));
    }

}