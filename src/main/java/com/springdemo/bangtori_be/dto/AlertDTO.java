package com.springdemo.bangtori_be.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AlertDTO {
    /** 알림 제목 */
    private String title;
    /** 알림 메시지 */
    private String message;
    /** 알림 시간 (Unix epoch seconds) */
    private Long time;
}
