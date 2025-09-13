package com.springdemo.bangtori_be.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TipDTO {
    /** 생활 습관 제목 */
    private String habitTitle;
    /** 생활 습관 내용 */
    private String habitDescription;

    /** 청소 팁 제목 */
    private String cleaningTitle;
    /** 청소 팁 내용 */
    private String cleaningDescription;
    /** 청소 팁 태그 (서버는 태그만 주고, 프런트에서 태그별 더미 확장 가능) */
    private String cleaningTag;
}