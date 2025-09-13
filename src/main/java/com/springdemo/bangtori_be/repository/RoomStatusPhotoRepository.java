package com.springdemo.bangtori_be.repository;

import com.springdemo.bangtori_be.model.RoomStatusPhoto;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoomStatusPhotoRepository extends MongoRepository<RoomStatusPhoto, String> {
}
