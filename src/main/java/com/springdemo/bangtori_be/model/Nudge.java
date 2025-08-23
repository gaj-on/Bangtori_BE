package com.springdemo.bangtori_be.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

// model/Nudge.java
@Document("nudge") @Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@CompoundIndex(name="status_idx", def="{ 'status': 1 }")
public class Nudge {
    @Id
    private String id;
    private String userId;           // MVP: 고정 "u1"
    private String type;             // score_low, tag_repeat...
    private String message;
    private String status;           // pending, ack
    private Instant createdAt;
    private Instant ackAt;
}
