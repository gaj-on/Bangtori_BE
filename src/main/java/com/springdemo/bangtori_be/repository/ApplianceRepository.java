package com.springdemo.bangtori_be.repository;

import com.springdemo.bangtori_be.model.Appliance;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ApplianceRepository extends MongoRepository<Appliance, String> {
}
