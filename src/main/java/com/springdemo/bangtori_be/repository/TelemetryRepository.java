package com.springdemo.bangtori_be.repository;

import com.springdemo.bangtori_be.model.Telemetry;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.List;

public interface TelemetryRepository extends MongoRepository<Telemetry, String> {
    List<Telemetry> findByDeviceSnAndTsBetween(String deviceSn, Instant from, Instant to);
}