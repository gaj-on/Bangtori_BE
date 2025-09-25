package com.springdemo.bangtori_be.repository;

import com.springdemo.bangtori_be.model.AppNotification;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AppNotificationRepository extends MongoRepository<AppNotification, String> {
    List<AppNotification> findByIsReadFalseOrderByCreatedAtDesc();
    List<AppNotification> findAllByOrderByCreatedAtDesc();
}
