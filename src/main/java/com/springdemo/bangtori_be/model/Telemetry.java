package com.springdemo.bangtori_be.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Map;

// model/Telemetry.java
@Document("telemetry") @Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@CompoundIndexes({
        @CompoundIndex(name="dev_ts_idx", def="{ 'deviceSn': 1, 'ts': 1 }")
})
public class Telemetry {
    @Id
    private String id;
    private String deviceSn;
    private Instant ts;
    private Map<String,Object> sensors; // {temp,humi,dust,tvoc,co2}
}