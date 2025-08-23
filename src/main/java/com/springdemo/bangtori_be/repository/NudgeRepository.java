package com.springdemo.bangtori_be.repository;

import com.springdemo.bangtori_be.model.Nudge;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NudgeRepository extends MongoRepository<Nudge, String> {
    List<Nudge> findByStatus(String status);
}
