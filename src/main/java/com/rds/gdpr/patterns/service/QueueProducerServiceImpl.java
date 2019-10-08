package com.rds.gdpr.patterns.service;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.kafka.client.producer.KafkaProducerRecord;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class QueueProducerServiceImpl implements QueueProducerService {

    private final KafkaProducer<String, String> producer;

    public QueueProducerServiceImpl(Vertx vertx) {
        Map<String, String> config = new HashMap<>();
        config.put("bootstrap.servers", "localhost:9092");
        config.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("acks", "1");
        producer = KafkaProducer.create(vertx, config);
    }

    @Override
    public void create(String topic, JsonObject body, Handler<AsyncResult<Void>> resultHandler) {
        log.info("Kafka Queue Producer : {} : {}", topic, body);
        producer.write(KafkaProducerRecord.create("chat-messages", body.encode()), resultHandler);
    }

}
