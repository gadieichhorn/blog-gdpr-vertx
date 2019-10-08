package com.rds.gdpr.patterns;

import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Launcher {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new WebServerVerticle());
        vertx.deployVerticle(new QueueProducerVerticle());
        vertx.deployVerticle(new EncryptionVerticle());
        vertx.deployVerticle(new DecryptionVerticle());
    }

}
