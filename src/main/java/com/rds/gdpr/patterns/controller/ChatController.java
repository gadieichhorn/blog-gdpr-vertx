package com.rds.gdpr.patterns.controller;

import com.rds.gdpr.patterns.model.ChatMessage;
import com.rds.gdpr.patterns.service.ChatProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatProducer chatProducer;

    @PostMapping()
    private Mono create(@RequestBody ChatMessage message) {
        return chatProducer.send(message);
    }

}
