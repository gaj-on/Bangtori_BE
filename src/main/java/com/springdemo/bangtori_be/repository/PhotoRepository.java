package com.springdemo.bangtori_be.repository;

import com.springdemo.bangtori_be.model.Photo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PhotoRepository extends MongoRepository<Photo, String> {}

