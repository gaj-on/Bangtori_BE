package com.springdemo.bangtori_be.dto;

import lombok.*;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class DailyDTO {
    private long time;
    private ReportsDTO reports;

    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor @Builder
    public static class ReportsDTO {
        private String aiDailyReport;
        private List<String> aiAnalysis;
        private int aiDailyScore;
    }
}
