package com.springdemo.bangtori_be.repository;

import com.springdemo.bangtori_be.model.RoomStatusPhoto;
import com.springdemo.bangtori_be.model.RoomStatusPhoto.TimeOfDay;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoomStatusPhotoRepository extends MongoRepository<RoomStatusPhoto, String> {

    Optional<RoomStatusPhoto> findTopByOrderByCreatedAtDesc();

    Optional<RoomStatusPhoto> findTopByTimeOfDayOrderByCreatedAtDesc(TimeOfDay slot);

    // 특정 날짜 구간 + 슬롯 1장
    @Query(value = "{ 'createdAt': { $gte: ?0, $lt: ?1 }, 'timeOfDay': ?2 }", sort = "{ 'createdAt': -1 }")
    Optional<RoomStatusPhoto> findOneByDateRangeAndSlot(long startOfDayEpochSec, long nextDayEpochSec, TimeOfDay slot);

    // 동일 날짜/슬롯 중복 방지(업서트 대체용): 기존 것 제거
    @Query(value = "{ 'createdAt': { $gte: ?0, $lt: ?1 }, 'timeOfDay': ?2 }")
    List<RoomStatusPhoto> findAllByDateRangeAndSlot(long startOfDayEpochSec, long nextDayEpochSec, TimeOfDay slot);
}