package com.springdemo.bangtori_be.repository;

import com.springdemo.bangtori_be.model.CleaningTip;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CleaningTipRepository extends MongoRepository <CleaningTip, String> {
}
