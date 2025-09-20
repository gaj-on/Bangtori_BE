package com.springdemo.bangtori_be.repository;

import com.springdemo.bangtori_be.model.Appliance;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ApplianceRepository extends MongoRepository<Appliance, String> {
    Optional<Appliance> findByType(Appliance.ApplianceType type);
}
