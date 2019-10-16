package com.rds.gdpr.patterns.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.rds.gdpr.patterns.dto.ChatMessageDto;
import lombok.*;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatMessage {

    @NonNull
    private String from;

    @NonNull
    private String message;

    private String key;

    public static ChatMessageDto of(ChatMessage message) {
        return ChatMessageDto.builder()
                .from(message.from)
                .message(message.message)
                .build();
    }

    public static ChatMessage of(ChatMessageDto dto) {
        return ChatMessage.builder()
                .from(dto.getFrom())
                .message(dto.getMessage())
                .build();
    }

}
