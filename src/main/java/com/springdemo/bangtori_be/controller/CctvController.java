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
    public ResponseEntity<String> uploadBase64(@RequestBody Map<String, String> body,
                                               @RequestParam(required = false) TimeOfDay slot) {
        String b64 = body.get("imageBase64");
        if (b64 == null || b64.isBlank()) {
            return ResponseEntity.badRequest().body("imageBase64 required");
        }
        try {
            RoomStatusPhoto saved = cctvService.saveBase64(b64, slot);
            return ResponseEntity.ok(saved.getTimeOfDay() + " uploaded at " + saved.getCreatedAt());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body("Invalid image payload: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed");
        }
    }


    /** 최신 1장 */
    @GetMapping("/latest")
    public ResponseEntity<byte[]> latest() {
        return cctvService.findLatest()
                .map(snap -> ResponseEntity.ok()
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
