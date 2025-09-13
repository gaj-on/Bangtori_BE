package com.springdemo.bangtori_be.controller;

import com.springdemo.bangtori_be.dto.ApplianceDTO;
import com.springdemo.bangtori_be.model.Appliance;
import com.springdemo.bangtori_be.repository.ApplianceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/appliances")
@RequiredArgsConstructor
public class ApplianceController {

    private final ApplianceRepository applianceRepository;

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
}
