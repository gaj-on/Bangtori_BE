package com.springdemo.bangtori_be.controller;

import com.springdemo.bangtori_be.dto.MissionDTO;
import com.springdemo.bangtori_be.model.Mission;
import com.springdemo.bangtori_be.repository.MissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/missions")
@RequiredArgsConstructor
public class MissionController {

    private final MissionRepository missionRepository;

    private long now() { return Instant.now().getEpochSecond(); }

    @PostMapping
    public Mission create(@RequestBody MissionDTO dto) {
        Mission m = Mission.builder()
                .title(dto.getTitle())
                .points(dto.getPoints() == null ? 0 : dto.getPoints())
                .category(dto.getCategory())
                .build();
        m.setCreatedAt(now());
        return missionRepository.save(m);
    }

    @GetMapping
    public List<Mission> list() {
        return missionRepository.findAll();
    }
}
