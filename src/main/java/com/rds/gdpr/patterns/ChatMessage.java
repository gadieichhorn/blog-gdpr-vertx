package com.rds.gdpr.patterns;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Data
@ToString
@NoArgsConstructor
public class ChatMessage {

    private UUID key;

    private String message;

    @Builder
    public ChatMessage(UUID key, String message) {
        this.key = key;
        this.message = message;
    }
}
