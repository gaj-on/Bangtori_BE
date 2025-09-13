package com.springdemo.bangtori_be.repository;

import com.springdemo.bangtori_be.model.Telemetry;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TelemetryRepository extends MongoRepository<Telemetry, String> {

    Optional<Telemetry> findTopByOrderByTimeDesc();

    // ✅ time 범위를 한 번에 묶고, 정렬도 명시
    @Query(value = "{ 'time': { $gte: ?0, $lt: ?1 } }", sort = "{ 'time': 1 }")
    List<Telemetry> findRangeAsc(long fromEpochSec, long toExclusiveEpochSec);
}