package com.springdemo.bangtori_be.service;

import com.springdemo.bangtori_be.dto.TelemetryDTO;
import com.springdemo.bangtori_be.model.Device;
import com.springdemo.bangtori_be.model.Telemetry;
import com.springdemo.bangtori_be.repository.DeviceRepository;
import com.springdemo.bangtori_be.repository.TelemetryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;

// service/TelemetryService.java
@Service
@RequiredArgsConstructor
public class TelemetryService {
    private final TelemetryRepository teleRepo;

    public void ingest(TelemetryDTO dto) {
        teleRepo.save(Telemetry.builder()
                .createdAt(Instant.now())  // 저장 시각 기록
                .sensors(new HashMap<>(dto.getMetrics()))
                .build());
    }

    public Object getLatestAttribute(String attribute) {
        Telemetry latest = teleRepo.findTopByOrderByIdDesc()  // 최근 값 가져오기
                .orElseThrow(() -> new RuntimeException("No telemetry data found"));
        return latest.getSensors().get(attribute);
    }
}