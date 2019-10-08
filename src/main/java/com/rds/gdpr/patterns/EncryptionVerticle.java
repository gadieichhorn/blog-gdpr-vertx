package com.rds.gdpr.patterns;

import com.rds.gdpr.patterns.service.QueueProducerService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.serviceproxy.ServiceProxyBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class EncryptionVerticle extends AbstractVerticle {

    private QueueProducerService queueProducerService;

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        queueProducerService = new ServiceProxyBuilder(vertx)
                .setAddress("queue-producer.proxy")
                .build(QueueProducerService.class);

        vertx.eventBus().consumer("chat-service-inbound").handler(message -> {
            queueProducerService.create("chat", JsonObject.mapFrom(ChatMessage.builder()
                    .key(UUID.randomUUID())
                    .message(message.body().toString())
                    .build()), published -> {
                if (published.succeeded()) {
                    log.info("Published", published.result());
                } else {
                    log.error("Published error", published.cause());
                }
            });
        });

        startPromise.complete();
    }

    @Override
    public void stop(Promise<Void> stopPromise) throws Exception {
        stopPromise.complete();
    }

}
