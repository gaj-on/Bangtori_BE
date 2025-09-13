package com.springdemo.bangtori_be.repository;

import com.springdemo.bangtori_be.model.MonthlySummary;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MonthlySummaryRepository extends MongoRepository<MonthlySummary, String>{
}
