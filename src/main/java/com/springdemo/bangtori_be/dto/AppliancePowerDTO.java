package com.springdemo.bangtori_be.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AppliancePowerDTO {
    private Boolean fan;
    private Boolean ac;
    private Boolean robot;
    private Boolean heat;
}