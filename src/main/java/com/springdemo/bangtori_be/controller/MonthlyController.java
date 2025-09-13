package com.springdemo.bangtori_be.controller;

import com.springdemo.bangtori_be.dto.MonthlyDTO;
import com.springdemo.bangtori_be.model.MonthlySummary;
import com.springdemo.bangtori_be.repository.MonthlySummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/monthly-summaries")
@RequiredArgsConstructor
public class MonthlyController {

    private final MonthlySummaryRepository monthlySummaryRepository;

    private long now() { return Instant.now().getEpochSecond(); }

    @PostMapping
    public MonthlySummary create(@RequestBody MonthlyDTO dto) {
        MonthlySummary s = MonthlySummary.builder()
                .month(dto.getMonth())
                .evaluation(dto.getEvaluation())
                .highestScore(dto.getHighestScore() == null ? 0 : dto.getHighestScore())
                .lowestScore(dto.getLowestScore() == null ? 0 : dto.getLowestScore())
                .averageScore(dto.getAverageScore() == null ? 0.0 : dto.getAverageScore())
                .build();
        s.setCreatedAt(now());
        return monthlySummaryRepository.save(s);
    }

    @GetMapping
    public List<MonthlySummary> list() {
        return monthlySummaryRepository.findAll();
    }
}
