package com.rds.gdpr.patterns.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class ChatMessage {

    private String from;

    private String message;

    @Builder
    public ChatMessage(String from, String message) {
        this.from = from;
        this.message = message;
    }

}
