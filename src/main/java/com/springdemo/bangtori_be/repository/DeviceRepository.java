package com.springdemo.bangtori_be.repository;

import com.springdemo.bangtori_be.model.Device;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeviceRepository extends MongoRepository<Device, String> {}

