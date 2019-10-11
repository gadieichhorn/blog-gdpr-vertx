package com.rds.gdpr.patterns.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@ToString
@NoArgsConstructor
public class Binary {

    @JsonProperty("$binary")
    private byte[] data;

    @JsonProperty("$type")
    private int type;

    @Builder
    public Binary(byte[] data) {
        this.data = data;
        this.type = 0;
    }
}
