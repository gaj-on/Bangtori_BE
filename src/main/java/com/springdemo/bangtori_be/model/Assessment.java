package com.springdemo.bangtori_be.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

// model/Assessment.java
@Document("assessment") @Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@CompoundIndex(name="room_eval_idx", def="{ 'roomId': 1, 'evaluatedAt': -1 }")
public class Assessment {
    @Id
    private String id;
    private String roomId;
    private String photoUrl;
    private Integer score;           // 0~100
    private List<String> tags;
    private Instant evaluatedAt;
}