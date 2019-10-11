package com.rds.gdpr.patterns.vertx;

import com.github.javafaker.Faker;
import com.rds.gdpr.patterns.AbstractMongoTest;
import com.rds.gdpr.patterns.repository.UsersRepository;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@Slf4j
@ExtendWith(VertxExtension.class)
class UsersVerticleTest extends AbstractMongoTest {

    private UsersRepository usersRepository;
    private final Faker faker = new Faker();

    @BeforeEach
    void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
        JsonObject mogno = new JsonObject()
                .put("db_name", "gdpr")
                .put("connection_string", "mongodb://localhost:" + MONGO_PORT);

        vertx.deployVerticle(new UsersVerticle(), new DeploymentOptions().setConfig(mogno),
                testContext.succeeding(id -> testContext.completeNow()));

        usersRepository = new UsersRepository(MongoClient.createShared(vertx, mogno));
    }

    @Test
    void verticle_deployed(Vertx vertx, VertxTestContext testContext) throws Throwable {
        testContext.completeNow();
    }


}