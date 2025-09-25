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
    public RoomStatusPhoto saveBase64(String dataUrlOrBase64, TimeOfDay requestedSlot) {
        String b64 = stripDataUrlPrefix(dataUrlOrBase64);
        if (b64 == null || b64.isBlank()) {
            throw new IllegalArgumentException("imageBase64 is empty");
        }

        byte[] raw = Base64.getDecoder().decode(b64);
        if (raw.length == 0) {
            throw new IllegalArgumentException("decoded image is empty");
        }

        LocalDate today = LocalDate.now(ZONE);
        TimeOfDay slot = (requestedSlot != null) ? requestedSlot : resolveSlot(LocalTime.now(ZONE));

        RoomStatusPhoto doc = RoomStatusPhoto.builder()
                .date(today)
                .timeOfDay(slot)   // 원하면 유지, 필요 없다면 제거 가능
                .build();

        // JPEG 변환 대신 그대로 저장
        doc.setImage(raw);
        doc.setContentType("image/jpeg"); // 클라가 png 넣을 수도 있으니, 동적으로 contentType 감지하는 것도 방법
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
