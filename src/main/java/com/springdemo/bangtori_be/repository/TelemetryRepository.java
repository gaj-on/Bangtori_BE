package com.springdemo.bangtori_be.repository;

import com.springdemo.bangtori_be.model.Telemetry;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TelemetryRepository extends MongoRepository<Telemetry, String> {
    Optional<Telemetry> findTopByOrderByIdDesc();
}