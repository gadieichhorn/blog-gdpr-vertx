package com.rds.gdpr.patterns.service;

import com.github.javafaker.Faker;
import com.rds.gdpr.patterns.model.ChatMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.test.StepVerifier;

import java.time.Instant;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@EmbeddedKafka
public class ChatProducerTest {

    private final Faker faker = new Faker();

    @Autowired
    private ChatProducer producer;

    @Test
    public void send() {

        StepVerifier
                .create(producer.send(ChatMessage.builder()
                        .time(Instant.now())
                        .user(UUID.randomUUID().toString())
                        .message(faker.lorem().paragraph())
                        .build()))
                .expectComplete()
                .verify();


//        Flux<String> source = Flux.just("John", "Monica", "Mark", "Cloe", "Frank", "Casper", "Olivia", "Emily", "Cate")
//                .filter(name -> name.length() == 4)
//                .map(String::toUpperCase);
//
//        StepVerifier
//                .create(source)
//                .expectNext("JOHN")
//                .expectNextMatches(name -> name.startsWith("MA"))
//                .expectNext("CLOE", "CATE")
//                .expectComplete()
//                .verify();
    }
}