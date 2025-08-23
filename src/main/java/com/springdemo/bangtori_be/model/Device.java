package com.springdemo.bangtori_be.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;
import java.util.Map;

// model/Device.java
@Document("device") @Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@CompoundIndex(name="room_idx", def="{ 'roomId': 1 }")
public class Device {
    @Id private String sn;            // device serial number = PK
    private String roomId;            // "10"
    private String status;            // online/offline
    private Instant createdAt;
}








