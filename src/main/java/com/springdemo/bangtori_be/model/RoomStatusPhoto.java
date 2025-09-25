// src/main/java/com/springdemo/bangtori_be/model/RoomStatusPhoto.java
package com.springdemo.bangtori_be.model;

import lombok.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Document("room_status_photos")
@CompoundIndex(name = "uniq_date_slot", def = "{'date':1,'timeOfDay':1}", unique = true)
public class RoomStatusPhoto extends BaseDocument {

    public enum TimeOfDay { MORNING, LUNCH, EVENING }

    /** 촬영(저장) 일자 (Asia/Seoul 기준) */
    @Indexed
    private LocalDate date;

    /** 슬롯 (아침/점심/저녁) */
    @Indexed
    private TimeOfDay timeOfDay;

    /** 바이너리 이미지 (항상 JPEG로 저장) */
    private byte[] image;

    /** MIME 타입 (항상 image/jpeg 로 통일) */
    private String contentType;
}
