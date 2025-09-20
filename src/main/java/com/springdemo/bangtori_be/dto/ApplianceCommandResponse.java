package com.springdemo.bangtori_be.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ApplianceCommandResponse {
    private String type;        // "AC" 등
    private Boolean requestedOn;// 프런트가 요청한 상태
    private Boolean ack;        // 아두이노가 성공 ACK했는지
    private Boolean applied;    // DB에 반영되었는지 (보통 ack와 동일)
    private Boolean state;      // 반영 후 현재 상태(또는 기존 상태)
    private Long timestamp;     // 서버 처리 시각(epoch seconds)
    private String message;     // 사람이 읽기 쉬운 메시지
}
