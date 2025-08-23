package com.springdemo.bangtori_be.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

@Data
public class TelemetryDTO {
    @NotBlank
    private String sn;
    @NotNull
    private Long ts;
    @NotNull
    private Map<String, Double> metrics;
}
