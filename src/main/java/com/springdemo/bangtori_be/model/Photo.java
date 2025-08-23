package com.springdemo.bangtori_be.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

// model/Photo.java
@Document("photo") @Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Photo {
    @Id
    private String id;
    private String roomId;
    private String photoUrl;
    private Instant takenAt;
    private Instant createdAt;
}
