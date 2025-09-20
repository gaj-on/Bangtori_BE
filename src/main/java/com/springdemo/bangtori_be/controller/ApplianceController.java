package com.springdemo.bangtori_be.controller;

import com.springdemo.bangtori_be.dto.ApplianceCommandResponse;
import com.springdemo.bangtori_be.dto.ApplianceDTO;
import com.springdemo.bangtori_be.model.Appliance;
import com.springdemo.bangtori_be.repository.ApplianceRepository;
import com.springdemo.bangtori_be.service.ApplianceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/appliances")
@RequiredArgsConstructor
public class ApplianceController {

    private final ApplianceRepository applianceRepository;
    private final ApplianceService applianceService;

    private long now() { return Instant.now().getEpochSecond(); }

    @PostMapping
    public Appliance create(@RequestBody ApplianceDTO dto) {
        Appliance a = Appliance.builder()
                .type(dto.getType() == null
                        ? Appliance.ApplianceType.FAN
                        : Appliance.ApplianceType.valueOf(dto.getType().name()))
                .isOn(Boolean.TRUE.equals(dto.getIsOn()))
                .build();
        a.setCreatedAt(now());
        return applianceRepository.save(a);
    }

    @GetMapping
    public List<Appliance> list() {
        return applianceRepository.findAll();
    }


    // controller/ApplianceController.java (setPower 전용 엔드포인트 추가)
    @PostMapping("/power")
    public ResponseEntity<ApplianceCommandResponse> setPower(@RequestBody ApplianceDTO dto) {
        ApplianceCommandResponse res = applianceService.setPower(dto);
        if (Boolean.TRUE.equals(res.getAck())) {
            return ResponseEntity.ok(res);
        } else {
            // 아두이노 미응답/실패 → 502 Bad Gateway
            return ResponseEntity.status(502).body(res);
        }
    }

}
