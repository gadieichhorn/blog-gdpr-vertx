package com.rds.gdpr.patterns.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Binary {

    @JsonProperty("$binary")
    private byte[] data;

    @JsonProperty("$type")
    private int type;

}
