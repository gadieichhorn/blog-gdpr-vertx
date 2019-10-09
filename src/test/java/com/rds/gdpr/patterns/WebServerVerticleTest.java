package com.rds.gdpr.patterns;

import com.github.javafaker.Faker;
import com.rds.gdpr.patterns.dto.UserDto;
import com.rds.gdpr.patterns.vertx.WebServerVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@Slf4j
@ExtendWith(VertxExtension.class)
public class WebServerVerticleTest {

    private final Faker faker = new Faker();

    @BeforeEach
    void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
        vertx.deployVerticle(new WebServerVerticle(), testContext.succeeding(id -> testContext.completeNow()));
    }

    @Test
    void verticle_deployed(Vertx vertx, VertxTestContext testContext) throws Throwable {
        testContext.completeNow();
    }

    @RepeatedTest(1)
    void http_server_check_response(Vertx vertx, VertxTestContext testContext) {
        WebClient client = WebClient.create(vertx);

        client.post(8080, "localhost", "/api/users")
                .putHeader("content-type", "application/json")
                .sendJsonObject(JsonObject.mapFrom(UserDto.builder().name(faker.name().username()).build()), post -> {
                    if (post.succeeded()) {
                        log.info("POST: {}", post.result().bodyAsString());
//                        Assertions.assertEquals(201, post.result().statusCode());
                        client.get(8080, "localhost", "/api/users")
                                .as(BodyCodec.jsonArray())
                                .send(testContext.succeeding(response -> testContext.verify(() -> {
                                    Assertions.assertTrue(response.body().size() > 0);
                                    testContext.completeNow();
                                })));
                    } else {
                        log.error("Post failed", post.cause());
                        testContext.failNow(post.cause());
                    }
                });

    }

    @RepeatedTest(1)
    void getOneUser(Vertx vertx, VertxTestContext testContext) {
        WebClient client = WebClient.create(vertx);

        client
                .get(8080, "localhost", "/api/users/123123123")
                .as(BodyCodec.string())
                .send(testContext.succeeding(response -> testContext.verify(() -> {
//                    Assertions.assertEquals("", response.body());
                    Assertions.assertEquals(404, response.statusCode());
                    testContext.completeNow();
                })));
    }

    @RepeatedTest(1)
    void getAllUsers(Vertx vertx, VertxTestContext testContext) {
        WebClient client = WebClient.create(vertx);

        client
                .get(8080, "localhost", "/api/users")
                .as(BodyCodec.jsonArray())
                .send(testContext.succeeding(response -> testContext.verify(() -> {
                    Assertions.assertTrue(response.body().size() > 0);
                    testContext.completeNow();
                })));
    }
}