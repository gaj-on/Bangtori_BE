package com.springdemo.bangtori_be.service;

import com.springdemo.bangtori_be.dto.PhotoDTO;
import com.springdemo.bangtori_be.model.RoomStatusPhoto;
import com.springdemo.bangtori_be.model.RoomStatusPhoto.TimeOfDay;
import com.springdemo.bangtori_be.repository.RoomStatusPhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.time.*;

@Service
@RequiredArgsConstructor
public class CctvService {

    private final RoomStatusPhotoRepository repo;
    private final RestTemplate restTemplate = new RestTemplate();

    private static final ZoneId ZONE = ZoneId.of("Asia/Seoul"); // 🇰🇷
    private static final String SNAPSHOT_URL = "http://172.xx.xx.xx/snapshot.jpg";
    // 필요시 BasicAuth 헤더 등 추가

    public RoomStatusPhoto captureAndSave() {
        byte[] img = restTemplate.getForObject(SNAPSHOT_URL, byte[].class);
        long now = Instant.now().getEpochSecond();

        TimeOfDay slot = resolveSlot(Instant.ofEpochSecond(now));

        // 같은 날짜/슬롯은 1장만 유지: 기존 것들 삭제 후 새로 저장(업서트 느낌)
        long start = LocalDate.now(ZONE).atStartOfDay(ZONE).toEpochSecond();
        long next  = LocalDate.now(ZONE).plusDays(1).atStartOfDay(ZONE).toEpochSecond();

        repo.findAllByDateRangeAndSlot(start, next, slot)
                .forEach(p -> repo.deleteById(p.getId()));

        RoomStatusPhoto photo = RoomStatusPhoto.builder()
                .timeOfDay(slot)
                .image(img)
                .build();
        photo.setCreatedAt(now);
        return repo.save(photo);
    }

    public RoomStatusPhoto getLatest() {
        return repo.findTopByOrderByCreatedAtDesc()
                .orElseThrow(() -> new IllegalStateException("No snapshot found"));
    }

    public RoomStatusPhoto getTodaySlot(TimeOfDay slot) {
        long start = LocalDate.now(ZONE).atStartOfDay(ZONE).toEpochSecond();
        long next  = LocalDate.now(ZONE).plusDays(1).atStartOfDay(ZONE).toEpochSecond();
        return repo.findOneByDateRangeAndSlot(start, next, slot)
                .orElseThrow(() -> new IllegalStateException("No snapshot for " + slot));
    }

    public RoomStatusPhoto getDateSlot(LocalDate date, TimeOfDay slot) {
        long start = date.atStartOfDay(ZONE).toEpochSecond();
        long next  = date.plusDays(1).atStartOfDay(ZONE).toEpochSecond();
        return repo.findOneByDateRangeAndSlot(start, next, slot)
                .orElseThrow(() -> new IllegalStateException("No snapshot for " + date + " " + slot));
    }

    /** 슬롯 규칙(원하시면 조정 가능)
     *  MORNING: 06:00–10:59
     *  LUNCH  : 11:00–13:59
     *  EVENING: 18:00–20:59
     *  그 외 시간엔 가장 가까운 슬롯으로 지정하고 싶으면 분기 추가
     */
    private TimeOfDay resolveSlot(Instant instant) {
        LocalTime t = instant.atZone(ZONE).toLocalTime();
        if (!t.isBefore(LocalTime.of(11,0))) {
            if (t.isBefore(LocalTime.of(14,0))) return TimeOfDay.LUNCH;
        }
        if (!t.isBefore(LocalTime.of(18,0))) {
            if (t.isBefore(LocalTime.of(21,0))) return TimeOfDay.EVENING;
        }
        return TimeOfDay.MORNING;
    }

    // (선택) 자동 캡처: 매일 09:00 / 12:00 / 19:00
    @Scheduled(cron = "0 0 9,12,19 * * *", zone = "Asia/Seoul")
    public void scheduledCapture() { captureAndSave(); }


    // (이미 앞서 구현해둔) 업로드 저장 재사용
    public RoomStatusPhoto saveUploadedImage(byte[] jpegBytes, RoomStatusPhoto.TimeOfDay slot) {
        // ... 앞서 드린 saveUploadedImage(byte[], slot) 그대로 사용 ...
        // (동일 날짜+슬롯 1장 유지 로직 포함)
        throw new UnsupportedOperationException("reuse previous saveUploadedImage implementation");
    }



    // ----- 행렬 사용 파트 ----- //

    /** JSON 행렬 → JPEG 바이트 */
    public byte[] matrixToJpeg(PhotoDTO dto) {
        int w = dto.getWidth(), h = dto.getHeight(), c = dto.getChannels();
        int[] px = dto.getData();
        if (c != 1 && c != 3) throw new IllegalArgumentException("channels must be 1 or 3");
        if (px == null) throw new IllegalArgumentException("data is null");
        if (c == 1 && px.length != w*h) throw new IllegalArgumentException("data length must be width*height");
        if (c == 3 && px.length != w*h*3) throw new IllegalArgumentException("data length must be width*height*3");

        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        int i = 0;
        if (c == 1) {
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    int v = clamp(px[i++]);          // 0..255
                    int rgb = (v<<16) | (v<<8) | v;  // gray -> RGB
                    img.setRGB(x, y, rgb);
                }
            }
        } else { // c == 3
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    int r = clamp(px[i++]);
                    int g = clamp(px[i++]);
                    int b = clamp(px[i++]);
                    int rgb = (r<<16) | (g<<8) | b;
                    img.setRGB(x, y, rgb);
                }
            }
        }
        return toJpeg(img);
    }

    private static int clamp(int v) { return (v < 0 ? 0 : (v > 255 ? 255 : v)); }

    private static byte[] toJpeg(BufferedImage img) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ImageIO.write(img, "jpg", out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("JPEG encode failed", e);
        }
    }
}
