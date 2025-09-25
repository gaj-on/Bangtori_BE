package com.springdemo.bangtori_be.controller;

import com.springdemo.bangtori_be.dto.TelemetryDTO;
import com.springdemo.bangtori_be.repository.TelemetryRepository;
import com.springdemo.bangtori_be.service.ApplianceAutoService;
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

    // 하루 시계열: 기본값은 '오늘(Asia/Seoul)'
    @GetMapping("/daily")
    public ResponseEntity<Object> getDaily(@RequestParam(required = false) String date) {
        return ResponseEntity.ok(telemetryService.getDailySeries(date)); // date: "YYYY-MM-DD"
    }

    // 한 달 시계열: 기본값은 '이번 달(Asia/Seoul)'
    @GetMapping("/monthly")
    public ResponseEntity<Object> getMonthly(@RequestParam(required = false, name = "month") String yearMonth) {
        return ResponseEntity.ok(telemetryService.getMonthlySeries(yearMonth)); // month: "YYYY-MM"
    }

    // (선택) 임의 범위 시계열: from/to는 epoch seconds, [from, toExclusive)
    @GetMapping("/range")
    public ResponseEntity<Object> getRange(@RequestParam long from, @RequestParam long toExclusive) {
        return ResponseEntity.ok(telemetryService.getRangeSeries(from, toExclusive));
    }

}

