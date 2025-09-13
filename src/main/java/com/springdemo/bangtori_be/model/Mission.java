package com.springdemo.bangtori_be.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Document("missions")
public class Mission extends BaseDocument {
    private String title;   // 미션 이름
    private int points;     // 미션 점수 (정수 추천)
    private String category; // 예: "환기", "방청소 루틴" 등
}