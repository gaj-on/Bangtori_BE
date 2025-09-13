package com.springdemo.bangtori_be.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Document("notifications")
public class AppNotification extends BaseDocument {
    private String title;
    private String message;
    private long time; // 알림 시간(Unix epoch seconds)
}