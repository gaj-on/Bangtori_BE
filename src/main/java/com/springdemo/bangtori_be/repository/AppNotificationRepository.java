package com.springdemo.bangtori_be.repository;

import com.springdemo.bangtori_be.model.AppNotification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AppNotificationRepository extends MongoRepository<AppNotification, String> {
}
