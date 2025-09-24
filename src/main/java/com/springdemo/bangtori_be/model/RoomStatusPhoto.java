package com.springdemo.bangtori_be.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Document("room_status_photos")
public class RoomStatusPhoto extends BaseDocument {

    public enum TimeOfDay { MORNING, LUNCH, EVENING }

    @Indexed
    private TimeOfDay timeOfDay;

    private byte[] image; // 스냅샷 바이트
}