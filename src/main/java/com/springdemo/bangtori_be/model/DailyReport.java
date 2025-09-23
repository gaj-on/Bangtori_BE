package com.springdemo.bangtori_be.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@Document("daily_reports")
public class DailyReport extends BaseDocument {

    /** Unix epoch seconds */
    private long time;

    private Reports reports;

    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Reports {
        /** 자연어 요약 */
        private String aiDailyReport;
        /** 상세 분석 bullet list */
        private List<String> aiAnalysis;
        /** 일일 점수 (0~100) */
        private int aiDailyScore;
    }
}
