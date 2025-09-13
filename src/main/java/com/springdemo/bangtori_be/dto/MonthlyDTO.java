package com.springdemo.bangtori_be.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MonthlyDTO {
    /** 월 표기 (예: "2025-09") */
    private String month;
    /** 종합평가 내용 */
    private String evaluation;
    /** 월 최고 점수 */
    private Integer highestScore;
    /** 월 최하 점수 */
    private Integer lowestScore;
    /** 월 평균 점수 */
    private Double averageScore;
}