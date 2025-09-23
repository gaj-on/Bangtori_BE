package com.springdemo.bangtori_be.controller;

import com.springdemo.bangtori_be.dto.DailyDTO;
import com.springdemo.bangtori_be.model.DailyReport;
import com.springdemo.bangtori_be.repository.DailyReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/daily-reports")
@RequiredArgsConstructor
public class DailyReportController {

    private final DailyReportRepository dailyReportRepository;

    // ====== DTO <-> Entity 변환 ======
    private static DailyReport toEntity(DailyDTO dto) {
        if (dto == null) return null;
        DailyReport.Reports reports = null;
        if (dto.getReports() != null) {
            reports = DailyReport.Reports.builder()
                    .aiDailyReport(dto.getReports().getAiDailyReport())
                    .aiAnalysis(dto.getReports().getAiAnalysis())
                    .aiDailyScore(dto.getReports().getAiDailyScore())
                    .build();
        }
        return DailyReport.builder()
                .time(dto.getTime())
                .reports(reports)
                .build();
    }

    private static DailyDTO toDTO(DailyReport entity) {
        if (entity == null) return null;
        DailyDTO.ReportsDTO reports = null;
        if (entity.getReports() != null) {
            reports = DailyDTO.ReportsDTO.builder()
                    .aiDailyReport(entity.getReports().getAiDailyReport())
                    .aiAnalysis(entity.getReports().getAiAnalysis())
                    .aiDailyScore(entity.getReports().getAiDailyScore())
                    .build();
        }
        return DailyDTO.builder()
                .time(entity.getTime())
                .reports(reports)
                .build();
    }
    // ====== 변환 끝 ======

    /** 생성 */
    @PostMapping
    public DailyDTO create(@RequestBody DailyDTO body) {
        DailyReport saved = dailyReportRepository.save(toEntity(body));
        return toDTO(saved);
    }

    /** 전체 조회 */
    @GetMapping
    public List<DailyDTO> list() {
        return dailyReportRepository.findAll().stream()
                .map(DailyReportController::toDTO)
                .toList();
    }

    /** time 기준 최신 조회 */
    @GetMapping("/latest")
    public DailyDTO latest() {
        return dailyReportRepository.findAll().stream()
                .max(Comparator.comparingLong(DailyReport::getTime))
                .map(DailyReportController::toDTO)
                .orElse(null);
    }
}
