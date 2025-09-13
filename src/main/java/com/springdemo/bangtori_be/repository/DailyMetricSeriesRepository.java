package com.springdemo.bangtori_be.repository;

import com.springdemo.bangtori_be.model.DailyMetricSeries;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DailyMetricSeriesRepository extends MongoRepository<DailyMetricSeries, String> {
}
