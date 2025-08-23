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
@RequestMapping("/api/v1/rooms/{roomId}")
public class TelemetryController {
    private final TelemetryService telemetryService;

    @PostMapping("/telemetry")
    public ResponseEntity<Void> ingest(@PathVariable String roomId,
                                       @Valid @RequestBody TelemetryDTO dto) {
        telemetryService.ingest(roomId, dto);
        return ResponseEntity.accepted().build();
    }
}
