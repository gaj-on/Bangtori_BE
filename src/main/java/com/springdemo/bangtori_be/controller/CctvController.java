package com.springdemo.bangtori_be.controller;

import com.springdemo.bangtori_be.dto.PhotoDTO;
import com.springdemo.bangtori_be.model.RoomStatusPhoto;
import com.springdemo.bangtori_be.model.RoomStatusPhoto.TimeOfDay;
import com.springdemo.bangtori_be.service.CctvService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/cctv")
@RequiredArgsConstructor
public class CctvController {

    private final CctvService cctvService;

    /** 수동 캡처(테스트용) */
    @PostMapping("/capture")
    public ResponseEntity<String> capture() {
        RoomStatusPhoto saved = cctvService.captureAndSave();
        return ResponseEntity.ok(saved.getTimeOfDay() + " captured at " + saved.getCreatedAt());
    }

    /** 가장 최근 1장 */
    @GetMapping("/latest")
    public ResponseEntity<byte[]> latest() {
        RoomStatusPhoto snap = cctvService.getLatest();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                .body(snap.getImage());
    }

    /** 오늘의 특정 슬롯(MORNING/LUNCH/EVENING) */
    // usage: /api/cctv/today?slot=MORNING (or LUNCH, EVENING)
    @GetMapping("/today")
    public ResponseEntity<byte[]> today(@RequestParam TimeOfDay slot) {
        RoomStatusPhoto snap = cctvService.getTodaySlot(slot);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                .body(snap.getImage());
    }

    /** 특정 날짜(YYYY-MM-DD) + 슬롯 */
    @GetMapping("/by-date")
    public ResponseEntity<byte[]> byDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam TimeOfDay slot
    ) {
        RoomStatusPhoto snap = cctvService.getDateSlot(date, slot);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                .body(snap.getImage());
    }


    // usage: POST /api/cctv/capture/matrix
    /**
     *
     * JSON 형태로 행렬 업로드
     * e.g.)
     * '{
     *     "width": 2,
     *     "height": 1,
     *     "channels": 3,
     *     "data": [255,0,0,  0,255,0]
     *   }'
     *
     */
    @PostMapping(value = "/capture/matrix", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> uploadMatrix(
            @RequestBody @Valid PhotoDTO dto,
            @RequestParam(required = false) TimeOfDay slot
    ) {
        byte[] jpeg = cctvService.matrixToJpeg(dto);
        RoomStatusPhoto saved = cctvService.saveUploadedImage(jpeg, slot);
        return ResponseEntity.ok(saved.getTimeOfDay() + " uploaded at " + saved.getCreatedAt());
    }

    /** (선택) Base64로 올리고 싶을 때 */
    @PostMapping("/capture/upload-base64")
    public ResponseEntity<String> uploadBase64(@RequestBody Map<String, String> body) {
        String b64 = body.get("imageBase64");
        if (b64 == null) return ResponseEntity.badRequest().body("imageBase64 required");
        cctvService.saveUploadedImage(java.util.Base64.getDecoder().decode(b64), null);
        return ResponseEntity.ok("Uploaded");
    }
}