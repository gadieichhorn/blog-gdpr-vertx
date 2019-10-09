package com.rds.gdpr.patterns.vertx;

import com.rds.gdpr.patterns.model.ChatMessage;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.Json;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.kafka.client.producer.KafkaProducerRecord;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
public class EncryptionVerticle extends AbstractVerticle {

    private KafkaProducer<String, String> producer;

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        Map<String, String> config = new HashMap<>();
        config.put("bootstrap.servers", "localhost:9092");
        config.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("acks", "1");

        producer = KafkaProducer.create(vertx, config);

        vertx.eventBus().consumer("chat-service-inbound").handler(message ->
                producer.write(KafkaProducerRecord.create("chat-messages",
                        Json.encode(ChatMessage.builder()
                                .from(UUID.randomUUID().toString())
                                .message(message.body().toString())
                                .build())),
                        published -> {
                            if (published.succeeded()) {
                                log.info("Published", published.result());
                            } else {
                                log.error("Published error", published.cause());
                            }
                        }));

        startPromise.complete();
    }

    @Override
    public void stop(Promise<Void> stopPromise) throws Exception {
        stopPromise.complete();
    }

}
