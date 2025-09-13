package com.springdemo.bangtori_be.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Document("daily_metric_series")
public class DailyMetricSeries extends BaseDocument {
    private String name;            // "온도", "습도", "Co2", "미세먼지"
    private List<Double> scoreList; // 길이 24, 시간당 1개
    private String date;            // "2025-09-13" 등(옵션) — 같은 날 데이터 식별용
}
