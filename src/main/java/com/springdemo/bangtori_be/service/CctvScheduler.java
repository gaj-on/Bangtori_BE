package com.springdemo.bangtori_be.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CctvScheduler {
    private final CctvService cctvService;

    @Scheduled(cron = "0 0 9,13,19 * * *")
    public void scheduledCapture() {
        cctvService.captureAndSave();
    }
}
