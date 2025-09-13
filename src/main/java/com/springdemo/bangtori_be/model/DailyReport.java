package com.springdemo.bangtori_be.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Document("daily_reports")
public class DailyReport extends BaseDocument {
    private String date;                    // "2025-09-13"
    private List<DailyMetricSeries> metrics; // 임베드(간단히 처리)
    private List<RoomStatusPhoto> photos;    // 임베드
    private List<String> aiAnalysis;         // 줄글 3개 정도
}