package com.springdemo.bangtori_be.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.Map;


@Document("telemetry")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Telemetry {
    @Id
    private String id;

    @Indexed(name = "time_idx")
    private long time; // 측정 시각(Unix epoch seconds)

    private Map<String,Object> sensors; // {temp,humi,dust,tvoc,co2}
}