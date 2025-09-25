package com.springdemo.bangtori_be.controller;

import com.springdemo.bangtori_be.dto.ApplianceDTO;
import com.springdemo.bangtori_be.dto.AppliancePowerDTO;
import com.springdemo.bangtori_be.model.Appliance;
import com.springdemo.bangtori_be.repository.ApplianceRepository;
import com.springdemo.bangtori_be.service.ApplianceAutoService;
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
    private final ApplianceAutoService applianceAutoService;

    private long now() { return Instant.now().getEpochSecond(); }

//    @PostMapping
//    public Appliance create(@RequestBody ApplianceDTO dto) {
//        Appliance a = Appliance.builder()
//                .type(dto.getType() == null
//                        ? Appliance.ApplianceType.FAN
//                        : Appliance.ApplianceType.valueOf(dto.getType().name()))
//                .isOn(Boolean.TRUE.equals(dto.getIsOn()))
//                .build();
//        a.setCreatedAt(now());
//        return applianceRepository.save(a);
//    }

    @GetMapping
    public List<Appliance> list() {
        return applianceRepository.findAll();
    }


    @PostMapping("/power")
    public ResponseEntity<AppliancePowerDTO> applyPower(@RequestBody AppliancePowerDTO dto) {
        if (dto == null) {
            return ResponseEntity.badRequest().build();
        }

        saveOrUpdate(Appliance.ApplianceType.FAN, dto.getFan());
        saveOrUpdate(Appliance.ApplianceType.AC, dto.getAc());
        saveOrUpdate(Appliance.ApplianceType.ROBOT, dto.getRobot());
        saveOrUpdate(Appliance.ApplianceType.HEAT, dto.getHeat());

        // 그대로 다시 돌려주면 프런트/아두이노 쪽에서도 확인 가능
        return ResponseEntity.ok(dto);
    }

    private void saveOrUpdate(Appliance.ApplianceType type, Boolean isOn) {
        if (isOn == null) return; // null은 무시

        Appliance a = applianceRepository.findByType(type)
                .orElseGet(() -> Appliance.builder().type(type).isOn(false).build());
        a.setOn(isOn);
        applianceRepository.save(a);
    }



}
