package com.springdemo.bangtori_be.controller;

import com.springdemo.bangtori_be.dto.AlertDTO;
import com.springdemo.bangtori_be.model.AppNotification;
import com.springdemo.bangtori_be.repository.AppNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final AppNotificationRepository appNotificationRepository;

    private long now() { return Instant.now().getEpochSecond(); }

    /** 생성 */
    @PostMapping
    public ResponseEntity<AppNotification> create(@RequestBody AlertDTO dto) {
        AppNotification n = AppNotification.builder()
                .title(dto.getTitle())
                .message(dto.getMessage())
                .time(dto.getTime() == null ? now() : dto.getTime())
                .build();
        n.setCreatedAt(now());
        // 기본값: isRead=false
        AppNotification saved = appNotificationRepository.save(n);
        return ResponseEntity.ok(saved);
    }

    /** 목록(최신순) */
    @GetMapping
    public ResponseEntity<List<AppNotification>> list() {
        return ResponseEntity.ok(appNotificationRepository.findAllByOrderByCreatedAtDesc());
    }

    /** 미읽음만(최신순) */
    @GetMapping("/unread")
    public ResponseEntity<List<AppNotification>> unread() {
        return ResponseEntity.ok(appNotificationRepository.findByIsReadFalseOrderByCreatedAtDesc());
    }

    /** 단건 읽음 표시 (POST /{id}/read) */
    @PostMapping("/{id}/read")
    public ResponseEntity<AppNotification> markRead(@PathVariable String id) {
        return appNotificationRepository.findById(id)
                .map(n -> {
                    n.setRead(true);
                    return ResponseEntity.ok(appNotificationRepository.save(n));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /** (선택) 단건 읽음 해제 (POST /{id}/unread) */
    @PostMapping("/{id}/unread")
    public ResponseEntity<AppNotification> markUnread(@PathVariable String id) {
        return appNotificationRepository.findById(id)
                .map(n -> {
                    n.setRead(false);
                    return ResponseEntity.ok(appNotificationRepository.save(n));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /** (선택) PATCH 토글: { "isRead": true|false } */
    @PatchMapping("/{id}")
    public ResponseEntity<AppNotification> patchRead(@PathVariable String id,
                                                     @RequestBody ReadPatch body) {
        if (body == null || body.isRead == null) return ResponseEntity.badRequest().build();
        return appNotificationRepository.findById(id)
                .map(n -> {
                    n.setRead(body.isRead);
                    return ResponseEntity.ok(appNotificationRepository.save(n));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    private record ReadPatch(Boolean isRead) {}

    /** 삭제 (DELETE /{id}) */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable String id) {
        if (!appNotificationRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        appNotificationRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
