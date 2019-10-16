package com.rds.gdpr.patterns.dto;

import lombok.*;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDto {

    @NonNull
    private String from;

    @NonNull
    private String message;

}
