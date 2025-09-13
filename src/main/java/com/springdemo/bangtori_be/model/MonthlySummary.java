package com.springdemo.bangtori_be.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Document("monthly_summaries")
public class MonthlySummary extends BaseDocument {
    private String month;         // "2025-09" 형식 권장
    private String evaluation;    // 종합평가 내용
    private int highestScore;     // 월 최고 점수
    private int lowestScore;      // 월 최하 점수
    private double averageScore;  // 월 평균 점수
}