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
    private final DeviceRepository deviceRepo;
    private final TelemetryRepository teleRepo;

    public void ingest(String roomId, TelemetryDTO dto) {
        deviceRepo.findById(dto.getSn()).orElseGet(() ->
                deviceRepo.save(Device.builder()
                        .sn(dto.getSn()).roomId(roomId).status("online").createdAt(Instant.now()).build()));
        teleRepo.save(Telemetry.builder()
                .deviceSn(dto.getSn())
                .ts(Instant.ofEpochSecond(dto.getTs()))
                .sensors(new HashMap<>(dto.getMetrics()))
                .build());
    }
}