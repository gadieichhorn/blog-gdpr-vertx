package com.rds.gdpr.patterns.vertx;

import com.rds.gdpr.patterns.cipher.ChatMessageCipherHelper;
import com.rds.gdpr.patterns.model.ChatMessage;
import com.rds.gdpr.patterns.model.User;
import com.rds.gdpr.patterns.repository.UsersMongoRepository;
import com.rds.gdpr.patterns.repository.UsersRepository;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.Json;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.kafka.client.consumer.KafkaConsumer;
import io.vertx.kafka.client.consumer.KafkaConsumerRecord;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
public class DecryptionVerticle extends AbstractVerticle {

    private MongoClient mongoClient;
    private UsersRepository usersRepository;

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        mongoClient = MongoClient.createShared(vertx, config().getJsonObject("mongo"));
        usersRepository = new UsersMongoRepository(mongoClient);

        Map<String, String> config = new HashMap<>();
        config.put("bootstrap.servers", "localhost:9092");
        config.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        config.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        config.put("group.id", "chat");
        config.put("auto.offset.reset", "earliest");
        config.put("enable.auto.commit", "false");

        KafkaConsumer.create(vertx, config).handler(record ->
                record(record, chatMessage ->
                        user(chatMessage, user ->
                                ChatMessageCipherHelper.getInstance().decrypt(user, chatMessage, chatMessageDto -> {
                                    log.info("ChatMessageDto: {}", chatMessageDto);
                                    vertx.eventBus().publish("chat-service-outbound", Json.encode(chatMessage));
                                    vertx.eventBus().publish("chat-service-outbound", Json.encode(chatMessageDto));
                                }))))
                .subscribe("chat-messages", startPromise);
    }

    @Override
    public void stop(Promise<Void> stopPromise) throws Exception {
        stopPromise.complete();
    }

    private void record(KafkaConsumerRecord record, Consumer<ChatMessage> handler) {
        log.debug("Processing key={} value={} ,partition={} ,offset={}", record.key(), record.value(), record.partition(), record.offset());
        handler.accept(Json.decodeValue(record.value().toString(), ChatMessage.class));
    }

    private void user(ChatMessage chatMessage, Consumer<User> handler) {
        log.debug("ChatMessage: {}", chatMessage);
        usersRepository.findById(chatMessage.getFrom(), event -> handler.accept(event.result().get()));
    }

}
