package com.springdemo.bangtori_be.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ApplianceDTO {

    /** 가전 종류 */
    public enum ApplianceType { FAN, AC, ROBOT, HEAT }

    /** 가전 타입 (fan, ac, robot, heat) */
    private ApplianceType type;

    /** 전원 상태 */
    private Boolean isOn;
}