package com.springdemo.bangtori_be.controller;

import com.springdemo.bangtori_be.dto.AnalysisDTO;
import com.springdemo.bangtori_be.dto.DailyDTO;
import com.springdemo.bangtori_be.dto.ScoreDTO;
import com.springdemo.bangtori_be.model.DailyReport;
import com.springdemo.bangtori_be.model.DailyMetricSeries;
import com.springdemo.bangtori_be.model.RoomStatusPhoto;
import com.springdemo.bangtori_be.repository.DailyReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/daily-reports")
@RequiredArgsConstructor
public class DailyReportController {

    private final DailyReportRepository dailyReportRepository;

    private long now() { return Instant.now().getEpochSecond(); }

    @PostMapping
    public DailyReport create(@RequestBody DailyDTO dto) {
        // ScoreDTO -> DailyMetricSeries
        List<DailyMetricSeries> metrics = (dto.getScores() == null ? List.<ScoreDTO>of() : dto.getScores())
                .stream().map(s -> {
                    DailyMetricSeries d = DailyMetricSeries.builder()
                            .name(s.getName())
                            .scoreList(s.getScoreList())
                            .date(dto.getDate()) // ScoreDTO에는 date가 없으므로 DailyDTO.date로 통일
                            .build();
                    d.setCreatedAt(now());
                    return d;
                }).toList();

        // Photo -> RoomStatusPhoto
        List<RoomStatusPhoto> photos = (dto.getPhotos() == null ? List.<DailyDTO.Photo>of() : dto.getPhotos())
                .stream().map(p -> {
                    RoomStatusPhoto rp = RoomStatusPhoto.builder()
                            .takenAt(p.getTakenAt() == null ? now() : p.getTakenAt())
                            .status(p.getStatus())
                            .imagePath(p.getImagePath())
                            .build();
                    rp.setCreatedAt(now());
                    return rp;
                }).toList();

        // AnalysisDTO -> List<String>
        AnalysisDTO analysis = dto.getAnalysis();
        List<String> ai = (analysis == null || analysis.getAiAnalysis() == null)
                ? List.of()
                : analysis.getAiAnalysis();

        DailyReport dr = DailyReport.builder()
                .date(dto.getDate())
                .metrics(metrics)
                .photos(photos)
                .aiAnalysis(ai)
                .build();
        dr.setCreatedAt(now());
        return dailyReportRepository.save(dr);
    }

    @GetMapping
    public List<DailyReport> list() {
        return dailyReportRepository.findAll();
    }
}
