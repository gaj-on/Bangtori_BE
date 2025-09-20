// service/ApplianceServiceImpl.java
package com.springdemo.bangtori_be.service;

import com.springdemo.bangtori_be.dto.ApplianceCommandResponse;
import com.springdemo.bangtori_be.dto.ApplianceDTO;
import com.springdemo.bangtori_be.model.Appliance;
import com.springdemo.bangtori_be.repository.ApplianceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ApplianceServiceImpl implements ApplianceService {

    private final ApplianceRepository applianceRepository;
    private final ArduinoGateway arduinoGateway;

    private long now() { return Instant.now().getEpochSecond(); }

    @Override
    public ApplianceCommandResponse setPower(ApplianceDTO dto) {
        if (dto.getType() == null) {
            return ApplianceCommandResponse.builder()
                    .type(null).requestedOn(null).ack(false).applied(false)
                    .state(null).timestamp(now()).message("type is required").build();
        }
        Appliance.ApplianceType type = Appliance.ApplianceType.valueOf(dto.getType().name());
        boolean requestedOn = Boolean.TRUE.equals(dto.getIsOn());

        // 1) 아두이노에 명령 전송
        boolean ack = arduinoGateway.setPower(type, requestedOn);

        Appliance saved = null;
        boolean applied = false;

        // 2) ACK가 true일 때만 DB 반영 (업서트)
        if (ack) {
            saved = applianceRepository.findByType(type).orElse(null);
            if (saved == null) {
                saved = Appliance.builder().type(type).isOn(requestedOn).build();
                saved.setCreatedAt(now());
            } else {
                saved.setOn(requestedOn);
            }
            saved = applianceRepository.save(saved);
            applied = true;
        } else {
            // 실패 시 기존 상태 그대로 유지 (saved는 null 또는 기존 조회 결과로 내려줘도 됨)
            saved = applianceRepository.findByType(type).orElse(null);
        }

        return ApplianceCommandResponse.builder()
                .type(type.name())
                .requestedOn(requestedOn)
                .ack(ack)
                .applied(applied)
                .state(saved != null ? saved.isOn() : null)
                .timestamp(now())
                .message(ack ? "Device acknowledged and state updated."
                        : "Device failed to acknowledge; state unchanged.")
                .build();
    }
}
