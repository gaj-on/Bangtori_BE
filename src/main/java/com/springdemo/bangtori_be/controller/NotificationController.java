package com.springdemo.bangtori_be.controller;

import com.springdemo.bangtori_be.dto.AlertDTO;
import com.springdemo.bangtori_be.model.AppNotification;
import com.springdemo.bangtori_be.repository.AppNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final AppNotificationRepository appNotificationRepository;

    private long now() { return Instant.now().getEpochSecond(); }

    @PostMapping
    public AppNotification create(@RequestBody AlertDTO dto) {
        AppNotification n = AppNotification.builder()
                .title(dto.getTitle())
                .message(dto.getMessage())
                .time(dto.getTime() == null ? now() : dto.getTime())
                .build();
        n.setCreatedAt(now());
        return appNotificationRepository.save(n);
    }

    @GetMapping
    public List<AppNotification> list() {
        return appNotificationRepository.findAll();
    }
}