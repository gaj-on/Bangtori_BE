package com.springdemo.bangtori_be.dto;

import lombok.*;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DailyDTO {
    /** 해당 일자 (예: "2025-09-13") */
    private String date;

    /** 일일 지표 점수(24시간 x 여러 항목) */
    private List<ScoreDTO> scores;

    /** 방 상태 사진 목록 */
    private List<Photo> photos;

    /** AI 분석 결과 */
    private AnalysisDTO analysis;

    /** 일일 리포트 내 사진 아이템 */
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Photo {
        /** 찍은 시각 (Unix epoch seconds) */
        private Long takenAt;
        /** 방 상황 (예: "양호", "보통") */
        private String status;
        /** 이미지 경로/URL (nullable 허용) */
        private String imagePath;
    }
}
