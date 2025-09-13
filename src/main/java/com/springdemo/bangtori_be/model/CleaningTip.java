package com.springdemo.bangtori_be.model;


import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Document("cleaning_tips")
public class CleaningTip extends BaseDocument {
    private String habitTitle;        // 생활 습관 제목
    private String habitDescription;  // 생활 습관 내용

    private String cleaningTitle;       // 청소 팁 제목
    private String cleaningDescription; // 청소 팁 내용
    private String cleaningTag;         // 태그 (서버는 태그만 받고, 실제 목록은 프런트 더미처리도 가능)
}