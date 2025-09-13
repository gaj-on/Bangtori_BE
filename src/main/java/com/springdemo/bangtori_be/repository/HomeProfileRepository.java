package com.springdemo.bangtori_be.repository;

import com.springdemo.bangtori_be.model.HomeProfile;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HomeProfileRepository extends MongoRepository<HomeProfile, String> {
}
