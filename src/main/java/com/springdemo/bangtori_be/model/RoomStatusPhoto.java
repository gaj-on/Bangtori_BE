package com.springdemo.bangtori_be.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Document("room_status_photos")
public class RoomStatusPhoto extends BaseDocument {
    private long takenAt;        // 찍은 시각(Unix epoch seconds)
    private String status;       // "양호", "보통", ...
    private String imagePath;    // 파일/URL 경로 (nullable 가능)
}