package com.rds.gdpr.patterns;

import com.rds.gdpr.patterns.vertx.DecryptionVerticle;
import com.rds.gdpr.patterns.vertx.EncryptionVerticle;
import com.rds.gdpr.patterns.vertx.WebServerVerticle;
import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Launcher {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new WebServerVerticle());
        vertx.deployVerticle(new EncryptionVerticle());
        vertx.deployVerticle(new DecryptionVerticle());
    }

}
