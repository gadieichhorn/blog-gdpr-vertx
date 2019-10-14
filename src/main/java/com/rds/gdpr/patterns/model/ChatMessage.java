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

//    @Builder
//    public ChatMessage(@NonNull String from, @NonNull String message) {
//        this.from = from;
//        this.message = message;
//        final SecretKeySpec secretKey = new SecretKeySpec(UUID.randomUUID().toString().substring(0, 16).getBytes(), "AES");
//        this.key = Binary.builder().data(secretKey.getEncoded()).build();
//    }

}
