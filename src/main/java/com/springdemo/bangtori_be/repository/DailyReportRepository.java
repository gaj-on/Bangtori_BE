package com.springdemo.bangtori_be.repository;

import com.springdemo.bangtori_be.model.DailyReport;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DailyReportRepository extends MongoRepository<DailyReport, String> {
}
