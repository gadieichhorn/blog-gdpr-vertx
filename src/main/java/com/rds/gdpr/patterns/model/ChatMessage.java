package com.rds.gdpr.patterns.model;

import lombok.Builder;
import lombok.ToString;
import lombok.Value;

import java.time.Instant;

@Value
@ToString
public class ChatMessage {

    private Instant time;

    private String user;

    private String message;

    @Builder
    public ChatMessage(Instant time, String user, String message) {
        this.time = time;
        this.user = user;
        this.message = message;
    }

}
