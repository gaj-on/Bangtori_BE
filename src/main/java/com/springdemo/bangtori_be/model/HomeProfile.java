package com.springdemo.bangtori_be.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Document("home_profiles")
public class HomeProfile extends BaseDocument {
    private String userName;
    private String userTitle;              // 예: "방청소의 권위자"
    private Boolean notificationsEnabled;  // 알림 On/Off
}