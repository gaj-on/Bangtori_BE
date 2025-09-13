package com.springdemo.bangtori_be.controller;


import com.springdemo.bangtori_be.dto.TipDTO;
import com.springdemo.bangtori_be.model.CleaningTip;
import com.springdemo.bangtori_be.repository.CleaningTipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/tips")
@RequiredArgsConstructor
public class CleaningTipController {

    private final CleaningTipRepository cleaningTipRepository;

    private long now() { return Instant.now().getEpochSecond(); }

    @PostMapping
    public CleaningTip create(@RequestBody TipDTO dto) {
        CleaningTip t = CleaningTip.builder()
                .habitTitle(dto.getHabitTitle())
                .habitDescription(dto.getHabitDescription())
                .cleaningTitle(dto.getCleaningTitle())
                .cleaningDescription(dto.getCleaningDescription())
                .cleaningTag(dto.getCleaningTag())
                .build();
        t.setCreatedAt(now());
        return cleaningTipRepository.save(t);
    }

    @GetMapping
    public List<CleaningTip> list() {
        return cleaningTipRepository.findAll();
    }
}
