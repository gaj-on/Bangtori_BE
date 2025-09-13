package com.springdemo.bangtori_be.dto;

import lombok.*;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AnalysisDTO {
    /** AI 분석 결과 문장들 (예: 3개 정도) */
    private List<String> aiAnalysis;
}