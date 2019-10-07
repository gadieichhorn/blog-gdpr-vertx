package com.rds.gdpr.patterns.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rds.gdpr.patterns.model.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.serialization.UUIDSerializer;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;
import reactor.kafka.sender.SenderRecord;
import reactor.kafka.sender.SenderResult;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
public class ChatProducer {

    private final KafkaSender<UUID, String> kafkaSender;

    private ObjectMapper objectMapper = new ObjectMapper();

    public ChatProducer() {
        final Map<String, Object> producerProps = new HashMap<>();
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, UUIDSerializer.class);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        final SenderOptions<UUID, String> producerOptions = SenderOptions.create(producerProps);
        kafkaSender = KafkaSender.create(producerOptions);
    }

    public Mono send(final ChatMessage message) {
        return kafkaSender
                .send(Mono.just(SenderRecord.create(record(message), 1)))
                .doOnError(throwable -> log.error("Exception", throwable))
                .next();
    }

    private void results(SenderResult senderResult) {
        RecordMetadata metadata = senderResult.recordMetadata();
        log.info("Message {} sent successfully, topic-partition={}-{} offset={}",
                senderResult.correlationMetadata(),
                metadata.topic(),
                metadata.partition(),
                metadata.offset());
//        return senderResult;
    }

    private ProducerRecord record(ChatMessage message) {
        log.info("Message: {}", message);
        return new ProducerRecord("chat-messages", UUID.randomUUID(), toBinary(message));
    }

    private String toBinary(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("IllegalArgumentException", e);
            throw new IllegalArgumentException(e);
        }
    }

}
