package com.springdemo.bangtori_be.repository;

import com.springdemo.bangtori_be.model.RoomStatusPhoto;
import com.springdemo.bangtori_be.model.RoomStatusPhoto.TimeOfDay;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface RoomStatusPhotoRepository extends MongoRepository<RoomStatusPhoto, String> {

    // createdAt 최신 1건
    Optional<RoomStatusPhoto> findTopByOrderByCreatedAtDesc();

    Optional<RoomStatusPhoto> findByDateAndTimeOfDay(LocalDate date, TimeOfDay slot);
}
