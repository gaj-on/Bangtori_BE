package com.springdemo.bangtori_be.controller;

import com.springdemo.bangtori_be.dto.HomeDTO;
import com.springdemo.bangtori_be.model.HomeProfile;
import com.springdemo.bangtori_be.repository.HomeProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/home")
@RequiredArgsConstructor
public class HomeController {
    private final HomeProfileRepository repo;
    private long now(){ return Instant.now().getEpochSecond(); }

    @PostMapping
    public HomeProfile upsert(@RequestBody HomeDTO dto){
        HomeProfile hp = HomeProfile.builder()
                .userName(dto.getUserName())
                .userTitle(dto.getUserTitle())
                .notificationsEnabled(dto.getNotificationsEnabled()==null?Boolean.TRUE:dto.getNotificationsEnabled())
                .build();
        hp.setCreatedAt(now());
        return repo.save(hp);
    }

    @GetMapping
    public List<HomeProfile> list(){ return repo.findAll(); }
}