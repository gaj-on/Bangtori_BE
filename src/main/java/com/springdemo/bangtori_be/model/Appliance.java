package com.springdemo.bangtori_be.model;

import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Document("appliances")
public class Appliance extends BaseDocument {

    // 가전 종류
    // 0. FAN (선풍기)
    // 1. AC (에어컨)
    // 2. ROBOT (로봇청소기)
    // 3. HEAT (난방)
    public enum ApplianceType { FAN, AC, ROBOT, HEAT }

    @Indexed(unique = true)
    private ApplianceType type;

    private boolean isOn;
}