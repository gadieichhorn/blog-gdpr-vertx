package com.rds.gdpr.patterns.vertx;

import com.rds.gdpr.patterns.cipher.ChatMessageCipherHelper;
import com.rds.gdpr.patterns.dto.ChatMessageDto;
import com.rds.gdpr.patterns.model.User;
import com.rds.gdpr.patterns.repository.UsersMongoRepository;
import com.rds.gdpr.patterns.repository.UsersRepository;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.kafka.client.producer.KafkaProducerRecord;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
public class EncryptionVerticle extends AbstractVerticle {

    private KafkaProducer<String, String> producer;
    private MongoClient mongoClient;
    private UsersRepository usersRepository;

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        mongoClient = MongoClient.createShared(vertx, config().getJsonObject("mongo"));
        usersRepository = new UsersMongoRepository(mongoClient);

        Map<String, String> config = new HashMap<>();
        config.put("bootstrap.servers", "localhost:9092");
        config.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("acks", "1");

        producer = KafkaProducer.create(vertx, config);

        vertx.eventBus().<JsonObject>localConsumer("chat-service-inbound").handler(this::handler);

        startPromise.complete();
    }

    @Override
    public void stop(Promise<Void> stopPromise) throws Exception {
        mongoClient.close();
        stopPromise.complete();
    }

    private void handler(Message<JsonObject> message) {
        message(message, chatMessageDto ->
                user(chatMessageDto.getFrom(), user ->
                        ChatMessageCipherHelper.getInstance().encrypt(user, chatMessageDto, encrypted ->
                                producer.write(KafkaProducerRecord.create("chat-messages", Json.encode(encrypted)), published ->
                                        log.debug("Published: {}", chatMessageDto)))));
    }

    private void user(String name, Consumer<User> handler) {
        usersRepository.findByName(name, event -> handler.accept(event.result().get()));
    }

    private void message(Message<JsonObject> message, Consumer<ChatMessageDto> handler) {
        handler.accept(message.body().mapTo(ChatMessageDto.class));
    }
}
