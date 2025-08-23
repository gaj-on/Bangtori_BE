package com.springdemo.bangtori_be.repository;

import com.springdemo.bangtori_be.model.Assessment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.List;

public interface AssessmentRepository extends MongoRepository<Assessment, String> {
    List<Assessment> findByRoomIdAndEvaluatedAtBetween(String roomId, Instant from, Instant to);
    List<Assessment> findByRoomIdOrderByEvaluatedAtDesc(String roomId);
}

