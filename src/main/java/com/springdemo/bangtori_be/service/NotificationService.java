
package com.springdemo.bangtori_be.service;

import com.springdemo.bangtori_be.dto.AlertDTO;
import com.springdemo.bangtori_be.model.AppNotification;
import com.springdemo.bangtori_be.repository.AppNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final AppNotificationRepository repo;

    public List<AlertDTO> findAll() {
        return repo.findAllByOrderByCreatedAtDesc()
                .stream().map(this::toDto).toList();
    }

    public List<AlertDTO> findUnread() {
        return repo.findByIsReadFalseOrderByCreatedAtDesc()
                .stream().map(this::toDto).toList();
    }

    public AlertDTO markRead(String id, boolean read) {
        AppNotification n = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found: " + id));
        n.setRead(read);
        AppNotification saved = repo.save(n);
        return toDto(saved);
    }

    public void delete(String id) {
        if (!repo.existsById(id)) {
            throw new IllegalArgumentException("Notification not found: " + id);
        }
        repo.deleteById(id);
    }

    // (선택) 테스트/데모용 생성
    public AlertDTO create(String title, String message) {
        AppNotification n = AppNotification.builder()
                .title(title)
                .message(message)
                .isRead(false)
                .build();
        // BaseDocument.setCreatedAt(Instant.now().getEpochSecond()) 내부에서 하시거나, 여기서 세팅
        n.setCreatedAt(Instant.now().getEpochSecond());
        AppNotification saved = repo.save(n);
        return toDto(saved);
    }

    private AlertDTO toDto(AppNotification n) {
        return AlertDTO.builder()
                .title(n.getTitle())
                .message(n.getMessage())
                .time(n.getTime())
                .isRead(n.isRead())
                .build();
    }
}