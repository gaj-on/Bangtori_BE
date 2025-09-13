package com.springdemo.bangtori_be.dto;

import lombok.*;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ScoreDTO {
    /** 지표명 (예: "온도", "습도", "Co2", "미세먼지") */
    private String name;
    /** 24시간 점수 시계열 (길이 24 권장) */
    private List<Double> scoreList;
}
