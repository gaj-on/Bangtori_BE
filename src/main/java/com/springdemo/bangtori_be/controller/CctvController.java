package com.springdemo.bangtori_be.controller;

import com.springdemo.bangtori_be.model.RoomStatusPhoto;
import com.springdemo.bangtori_be.model.RoomStatusPhoto.TimeOfDay;
import com.springdemo.bangtori_be.service.CctvService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/cctv")
@RequiredArgsConstructor
public class CctvController {

    private final CctvService cctvService;

    /** Base64 업로드 (data:image/...;base64,xxx 또는 순수 base64) */
    // CctvController.java (/upload-base64만 수정)
    @PostMapping("/upload-base64")
    public ResponseEntity<Map<String, Object>> uploadBase64(
            @RequestBody Map<String, String> body,
            @RequestParam(required = false) RoomStatusPhoto.TimeOfDay slot
    ) {
        String b64 = body.get("imageBase64");
        if (b64 == null || b64.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "imageBase64 required"));
        }

        RoomStatusPhoto saved = cctvService.saveBase64(b64, slot);

        // byte[] → Base64 다시 변환해서 응답에 포함
        String encoded = java.util.Base64.getEncoder().encodeToString(saved.getImage());

        Map<String, Object> response = Map.of(
                "id", saved.getId(),
                "date", saved.getDate(),
                "slot", saved.getTimeOfDay(),
                "imageBase64", encoded,
                "size", saved.getImage() != null ? saved.getImage().length : 0,
                "createdAt", saved.getCreatedAt()
        );

        return ResponseEntity.ok(response);
    }



    /** 최신 1장 */
    @GetMapping("/latest")
    public ResponseEntity<byte[]> latest() {
        return cctvService.findLatest()
                .map(snap -> ResponseEntity.ok()
                        // 저장된 contentType 사용 가능하면 교체: snap.getContentType()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(snap.getImage()))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /** 오늘 슬롯(MORNING/LUNCH/EVENING) */
    @GetMapping("/today")
    public ResponseEntity<byte[]> today(@RequestParam TimeOfDay slot) {
        return cctvService.findTodaySlot(slot)
                .map(snap -> ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(snap.getImage()))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /** 특정 날짜 + 슬롯 */
    @GetMapping("/by-date")
    public ResponseEntity<byte[]> byDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam TimeOfDay slot) {
        return cctvService.findDateSlot(date, slot)
                .map(snap -> ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(snap.getImage()))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
