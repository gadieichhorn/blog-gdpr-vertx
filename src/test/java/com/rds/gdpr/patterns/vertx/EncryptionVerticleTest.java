package com.rds.gdpr.patterns.vertx;

import com.rds.gdpr.patterns.AbstractMongoTest;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(VertxExtension.class)
class EncryptionVerticleTest extends AbstractMongoTest {

    @BeforeEach
    void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
        JsonObject mogno = new JsonObject().put("mongo", mongoConfig);
        vertx.deployVerticle(new EncryptionVerticle(), new DeploymentOptions().setConfig(mogno),
                testContext.succeeding(id -> testContext.completeNow()));
    }

    @Test
    void verticle_deployed(Vertx vertx, VertxTestContext testContext) throws Throwable {
        testContext.completeNow();
    }

}