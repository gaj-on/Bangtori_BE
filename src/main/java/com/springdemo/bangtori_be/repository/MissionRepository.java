package com.springdemo.bangtori_be.repository;

import com.springdemo.bangtori_be.model.Mission;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MissionRepository extends MongoRepository<Mission, String> {}