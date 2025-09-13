package com.springdemo.bangtori_be.service;

import com.springdemo.bangtori_be.dto.TelemetryDTO;

public interface TelemetryService {
    void ingest(TelemetryDTO dto);

    Object getLatestAttribute(String attribute);
    TelemetryDTO getLatestDTO();

    // 새로 추가: 범위/일/월 시계열
    Object getRangeSeries(long fromEpochSec, long toExclusiveEpochSec);
    Object getDailySeries(String dateYmd);     // "2025-09-13"
    Object getMonthlySeries(String yearMonth); // "2025-09"
}