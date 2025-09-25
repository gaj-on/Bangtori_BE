// src/main/java/com/springdemo/bangtori_be/repository/RoomStatusPhotoRepository.java
package com.springdemo.bangtori_be.repository;

import com.springdemo.bangtori_be.model.RoomStatusPhoto;
import com.springdemo.bangtori_be.model.RoomStatusPhoto.TimeOfDay;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface RoomStatusPhotoRepository extends MongoRepository<RoomStatusPhoto, String> {

    Optional<RoomStatusPhoto> findTopByOrderByCreatedAtDesc();

    Optional<RoomStatusPhoto> findByDateAndTimeOfDay(LocalDate date, TimeOfDay slot);
}
