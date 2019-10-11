package com.rds.gdpr.patterns;

import com.rds.gdpr.patterns.vertx.DecryptionVerticle;
import com.rds.gdpr.patterns.vertx.EncryptionVerticle;
import com.rds.gdpr.patterns.vertx.UsersVerticle;
import com.rds.gdpr.patterns.vertx.WebServerVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Launcher {

    private static final JsonObject config = new JsonObject()
            .put("mongo", new JsonObject()
                    .put("connection_string", "mongodb://localhost:27017")
                    .put("db_name", "users"));

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new UsersVerticle(), new DeploymentOptions().setConfig(config.getJsonObject("mongo")));
        vertx.deployVerticle(new WebServerVerticle(), new DeploymentOptions().setConfig(config));
        vertx.deployVerticle(new EncryptionVerticle(), new DeploymentOptions().setConfig(config));
        vertx.deployVerticle(new DecryptionVerticle(), new DeploymentOptions().setConfig(config));
    }

}
