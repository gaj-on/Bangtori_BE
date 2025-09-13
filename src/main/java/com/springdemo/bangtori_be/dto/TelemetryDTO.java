package com.springdemo.bangtori_be.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

@Data
public class TelemetryDTO {
    private Map<String, Double> metrics;
    private Long time;
}
