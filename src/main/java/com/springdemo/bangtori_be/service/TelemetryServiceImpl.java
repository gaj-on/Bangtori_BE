package com.springdemo.bangtori_be.service;


import com.springdemo.bangtori_be.dto.TelemetryDTO;
import com.springdemo.bangtori_be.model.Telemetry;
import com.springdemo.bangtori_be.repository.TelemetryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TelemetryServiceImpl implements TelemetryService {

    private final TelemetryRepository telemetryRepository;

    private static final ZoneId ZONE = ZoneId.of("Asia/Seoul");

    private long now() { return Instant.now().getEpochSecond(); }

    @Override
    public void ingest(TelemetryDTO dto) {
        long time = (dto.getTime() != null && dto.getTime() > 0) ? dto.getTime() : now();
        Map<String, Object> sensors = new HashMap<>();
        if (dto.getMetrics() != null) {
            dto.getMetrics().forEach((k, v) -> { if (v != null) sensors.put(k, v); });
        }
        Telemetry entity = Telemetry.builder()
                .time(time)
                .sensors(sensors)
                .build();
        telemetryRepository.save(entity);
    }

    @Override
    public Object getLatestAttribute(String attribute) {
        var opt = telemetryRepository.findTopByOrderByTimeDesc();

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("attribute", attribute);

        if (opt.isEmpty()) {
            body.put("value", null);
            body.put("time", null);
            body.put("present", false);
            return body;
        }

        Telemetry t = opt.get();
        Object raw = (t.getSensors() == null) ? null : t.getSensors().get(attribute);
        Double value = toDoubleOrNull(raw);

        body.put("value", value);                    // null 허용
        body.put("time", t.getTime());               // primitive long → autoboxing OK
        body.put("present", t.getSensors()!=null && t.getSensors().containsKey(attribute));
        return body;
    }


    @Override
    public TelemetryDTO getLatestDTO() {
        return telemetryRepository.findTopByOrderByTimeDesc()
                .map(t -> {
                    Map<String, Double> metrics = new HashMap<>();
                    if (t.getSensors()!=null) {
                        t.getSensors().forEach((k,v)-> {
                            Double d = toDoubleOrNull(v);
                            if (d!=null) metrics.put(k,d);
                        });
                    }
                    TelemetryDTO dto = new TelemetryDTO();
                    dto.setMetrics(metrics);
                    dto.setTime(t.getTime());
                    return dto;
                })
                .orElse(null);
    }

    // ---------- 새로 추가된 기능들 ----------

    @Override
    public Object getRangeSeries(long fromEpochSec, long toExclusiveEpochSec) {
        List<Telemetry> rows = telemetryRepository
                .findRangeAsc(fromEpochSec, toExclusiveEpochSec);
        return buildSeriesResponse(rows, fromEpochSec, toExclusiveEpochSec, "range");
    }

    @Override
    public Object getDailySeries(String dateYmd) {
        LocalDate d = (dateYmd == null || dateYmd.isBlank())
                ? LocalDate.now(ZONE) : LocalDate.parse(dateYmd);
        long from = d.atStartOfDay(ZONE).toEpochSecond();
        long toExclusive = d.plusDays(1).atStartOfDay(ZONE).toEpochSecond();

        List<Telemetry> rows = telemetryRepository
                .findRangeAsc(from, toExclusive);
        return buildSeriesResponse(rows, from, toExclusive, "daily");
    }

    @Override
    public Object getMonthlySeries(String yearMonth) {
        YearMonth ym = (yearMonth == null || yearMonth.isBlank())
                ? YearMonth.now(ZONE) : YearMonth.parse(yearMonth);
        long from = ym.atDay(1).atStartOfDay(ZONE).toEpochSecond();
        long toExclusive = ym.plusMonths(1).atDay(1).atStartOfDay(ZONE).toEpochSecond();

        List<Telemetry> rows = telemetryRepository
                .findRangeAsc(from, toExclusive);
        return buildSeriesResponse(rows, from, toExclusive, "monthly");
    }

    // 시계열 응답: attribute별 [{time, value}, ...]
    private Map<String, Object> buildSeriesResponse(List<Telemetry> rows, long from, long toExclusive, String granularity) {
        Map<String, List<Map<String, Object>>> series = new LinkedHashMap<>();

        for (Telemetry t : rows) {
            if (t.getSensors()==null) continue;
            long ts = t.getTime();
            for (Map.Entry<String, Object> e : t.getSensors().entrySet()) {
                Double val = toDoubleOrNull(e.getValue());
                if (val == null) continue;
                series.computeIfAbsent(e.getKey(), k -> new ArrayList<>())
                        .add(Map.of("time", ts, "value", val));
            }
        }

        return Map.of(
                "granularity", granularity,
                "timezone", ZONE.getId(),
                "from", from,
                "toExclusive", toExclusive,
                "count", rows.size(),
                "series", series   // 예: { "temp": [{time:..., value:...}, ...], "humi": [...], ... }
        );
    }

    private Double toDoubleOrNull(Object o) {
        if (o == null) return null;
        if (o instanceof Number n) return n.doubleValue();
        if (o instanceof String s) {
            try { return Double.parseDouble(s); } catch (NumberFormatException ignored) {}
        }
        return null;
    }
}