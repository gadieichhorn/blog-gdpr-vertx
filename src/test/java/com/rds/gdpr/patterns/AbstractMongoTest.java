package com.rds.gdpr.patterns;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.junit5.VertxExtension;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;

@Slf4j
@ExtendWith(VertxExtension.class)
public abstract class AbstractMongoTest {

    protected static JsonObject mongoConfig;
    protected MongoClient mongoClient;
    protected static MongodProcess MONGO;
    protected static int MONGO_PORT = 12345;

    @BeforeAll
    public static void initialize() throws IOException {
        log.info("Starting MongoDB Embedded server: {}", MONGO_PORT);
        MongodStarter starter = MongodStarter.getDefaultInstance();
        IMongodConfig mongodConfig = new MongodConfigBuilder()
                .version(Version.Main.PRODUCTION)
                .net(new Net(MONGO_PORT, Network.localhostIsIPv6()))
                .build();

        MongodExecutable mongodExecutable = starter.prepare(mongodConfig);
        MONGO = mongodExecutable.start();

        mongoConfig = new JsonObject()
                .put("db_name", "gdpr")
                .put("connection_string", "mongodb://localhost:" + MONGO_PORT);

    }

    @AfterAll
    public static void shutdown() {
        log.info("Stopping MongoDB Embedded server: {}", MONGO_PORT);
        MONGO.stop();
    }

    @BeforeEach
    public void beforeEach(Vertx vertx) {
        mongoClient = MongoClient.createShared(vertx, mongoConfig);
    }

    @AfterEach
    public void afterEach() {
        mongoClient.close();
    }

}
