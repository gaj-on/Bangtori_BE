package com.springdemo.bangtori_be.controller;

import com.springdemo.bangtori_be.dto.TelemetryDTO;
import com.springdemo.bangtori_be.service.TelemetryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// controller/TelemetryController.java
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/telemetry")
public class TelemetryController {
    private final TelemetryService telemetryService;

    @PostMapping
    public ResponseEntity<Void> ingest(@Valid @RequestBody TelemetryDTO dto) {
        telemetryService.ingest(dto);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/temp")
    public ResponseEntity<Object> getTemp() {
        return ResponseEntity.ok(telemetryService.getLatestAttribute("temp"));
    }

    @GetMapping("/humi")
    public ResponseEntity<Object> getHumi() {
        return ResponseEntity.ok(telemetryService.getLatestAttribute("humi"));
    }

    @GetMapping("/dust")
    public ResponseEntity<Object> getDust() {
        return ResponseEntity.ok(telemetryService.getLatestAttribute("dust"));
    }

    @GetMapping("/tvoc")
    public ResponseEntity<Object> getTvoc() {
        return ResponseEntity.ok(telemetryService.getLatestAttribute("tvoc"));
    }

    @GetMapping("/co2")
    public ResponseEntity<Object> getCo2() {
        return ResponseEntity.ok(telemetryService.getLatestAttribute("co2"));
    }

    @GetMapping("/timestamp")
    public ResponseEntity<Object> getLatestDTO() { return ResponseEntity.ok(telemetryService.getLatestDTO()); }


}

