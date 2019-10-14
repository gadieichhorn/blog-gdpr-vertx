package com.rds.gdpr.patterns.model;

import lombok.*;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {

    @NonNull
    private String from;

    @NonNull
    private String message;

    private String key;

}
