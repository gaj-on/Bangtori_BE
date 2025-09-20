package com.springdemo.bangtori_be.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Document("daily_scores")
public class DailyScore {
    @Id
    private String id;

    /** 대상 일자: "YYYY-MM-DD" (Asia/Seoul 기준) */
    private String date;

    /** metric별 24개 점수 (0~100) */
    private List<ScoreItem> scores;

    /** AI 점수가 산출된 시각 (Unix epoch seconds) */
    private long computedAt;

    /** (선택) 어떤 모델/버전으로 채점했는지 */
    private String aiModel;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class ScoreItem {
        private String name;            // 예: "temp", "humi", "co2"...
        private List<Double> scoreList; // 길이 24 권장
    }
}