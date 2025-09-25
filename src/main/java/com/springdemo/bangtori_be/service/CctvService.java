// src/main/java/com/springdemo/bangtori_be/service/CctvService.java
package com.springdemo.bangtori_be.service;

import com.springdemo.bangtori_be.model.RoomStatusPhoto;
import com.springdemo.bangtori_be.model.RoomStatusPhoto.TimeOfDay;
import com.springdemo.bangtori_be.repository.RoomStatusPhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.*;
import java.util.Base64;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CctvService {

    private final RoomStatusPhotoRepository repo;
    private static final ZoneId ZONE = ZoneId.of("Asia/Seoul");

    /* ========== 조회 ========== */

    public Optional<RoomStatusPhoto> findLatest() {
        return repo.findTopByOrderByCreatedAtDesc()
                .filter(p -> p.getImage() != null && p.getImage().length > 0);
    }

    public Optional<RoomStatusPhoto> findTodaySlot(RoomStatusPhoto.TimeOfDay slot) {
        LocalDate today = LocalDate.now(ZONE);
        return repo.findByDateAndTimeOfDay(today, slot);
    }

    public Optional<RoomStatusPhoto> findDateSlot(LocalDate date, RoomStatusPhoto.TimeOfDay slot) {
        return repo.findByDateAndTimeOfDay(date, slot);
    }

    /* ========== 저장(Base64만) ========== */

    /**
     * data:image/...;base64,xxx 또는 순수 base64 둘 다 허용
     * - 서버에서 무조건 JPEG로 재인코딩하여 저장
     * - slot이 null이면 현재 시간대 기준으로 자동 결정
     */
    // CctvService.java (saveBase64 교체)
    public RoomStatusPhoto saveBase64(String dataUrlOrBase64, TimeOfDay requestedSlot) {
        // 1) 접두부 제거 + 디코딩
        String b64 = stripDataUrlPrefix(dataUrlOrBase64);
        if (b64 == null || b64.isBlank()) {
            throw new IllegalArgumentException("imageBase64 is empty");
        }
        byte[] raw;
        try {
            raw = Base64.getDecoder().decode(b64);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("imageBase64 decode failed", e);
        }
        if (raw.length == 0) {
            throw new IllegalArgumentException("decoded image is empty");
        }

        // 2) 이미지 디코딩 → JPEG 재인코딩
        BufferedImage bi = readImage(raw);          // read 실패 시 IllegalArgumentException
        byte[] jpegBytes = toJpeg(bi);
        if (jpegBytes == null || jpegBytes.length == 0) {
            throw new IllegalStateException("JPEG encode produced empty bytes");
        }

        // 3) 날짜/슬롯 결정 및 업서트
        LocalDate today = LocalDate.now(ZONE);
        TimeOfDay slot = (requestedSlot != null) ? requestedSlot : resolveSlot(LocalTime.now(ZONE));

        RoomStatusPhoto doc = repo.findByDateAndTimeOfDay(today, slot)
                .orElse(RoomStatusPhoto.builder()
                        .date(today)
                        .timeOfDay(slot)
                        .build());

        doc.setImage(jpegBytes);
        doc.setContentType("image/jpeg");
        doc.setCreatedAt(Instant.now().getEpochSecond());

        return repo.save(doc);
    }


    /* ========== 내부 유틸 ========== */

    private static String stripDataUrlPrefix(String s) {
        int idx = s.indexOf("base64,");
        return (idx >= 0) ? s.substring(idx + "base64,".length()) : s;
    }

    private static BufferedImage readImage(byte[] bytes) {
        try {
            BufferedImage bi = ImageIO.read(new ByteArrayInputStream(bytes));
            if (bi == null) throw new IllegalArgumentException("Invalid image payload");
            return bi;
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot decode image", e);
        }
    }

    private static byte[] toJpeg(BufferedImage img) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ImageIO.write(img, "jpg", out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("JPEG encode failed", e);
        }
    }

    private static TimeOfDay resolveSlot(LocalTime now) {
        int h = now.getHour();
        if (h < 11) return TimeOfDay.MORNING;
        if (h < 16) return TimeOfDay.LUNCH;
        return TimeOfDay.EVENING;
    }
}
