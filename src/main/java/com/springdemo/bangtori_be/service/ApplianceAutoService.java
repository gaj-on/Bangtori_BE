package com.springdemo.bangtori_be.service;

import com.springdemo.bangtori_be.model.Appliance;
import com.springdemo.bangtori_be.repository.ApplianceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ApplianceAutoService {

    private final ApplianceRepository applianceRepository;

    // 기준값
    private static final double CO2_FAN_ON  = 1000.0; // ppm
    private static final double TVOC_FAN_ON = 400.0;  // ppb
    private static final double PM25_DIRTY  = 75.0;   // μg/m³

    private static final double TEMP_AC_ON   = 28.0;  // ℃
    private static final double TEMP_HEAT_ON = 18.0;  // ℃
    private static final double HUMI_WET     = 70.0;  // %

    public void applyAutoControl(Map<String, Object> sensors) {
        Double temp = d(sensors, "temp");
        Double humi = d(sensors, "humi");
        Double co2  = d(sensors, "co2");
        Double tvoc = d(sensors, "tvoc");
        Double dust = d(sensors, "dust"); // PM2.5

        // 1) FAN: CO2>1000 OR TVOC>400 OR PM2.5>75
        Boolean fanOn = or(gt(co2, CO2_FAN_ON), gt(tvoc, TVOC_FAN_ON), gt(dust, PM25_DIRTY));
        if (fanOn != null) upsertOn(Appliance.ApplianceType.FAN, fanOn);

        // 2) AC: temp>28
        Boolean acOn = gt(temp, TEMP_AC_ON);
        if (acOn != null) upsertOn(Appliance.ApplianceType.AC, acOn);

        // 3) HEAT: temp<18 OR humi>70
        Boolean heatOn = or(lt(temp, TEMP_HEAT_ON), gt(humi, HUMI_WET));
        if (heatOn != null) upsertOn(Appliance.ApplianceType.HEAT, heatOn);

        // 4) ROBOT: PM2.5>75 AND 현재 OFF이면 → ON (그 외엔 상태 유지)
        if (dust != null && dust > PM25_DIRTY) {
            boolean currentlyOn = applianceRepository.findByType(Appliance.ApplianceType.ROBOT)
                    .map(Appliance::isOn).orElse(false);
            if (!currentlyOn) upsertOn(Appliance.ApplianceType.ROBOT, true);
        }
    }

    // ---------- helpers ----------
    private void upsertOn(Appliance.ApplianceType type, boolean desired) {
        Appliance a = applianceRepository.findByType(type)
                .orElseGet(() -> Appliance.builder().type(type).isOn(false).build());
        if (!Boolean.valueOf(desired).equals(a.isOn())) {
            a.setOn(desired);
            applianceRepository.save(a);
        }
    }

    private Double d(Map<String, Object> m, String k) {
        if (m == null) return null;
        Object v = m.get(k);
        if (v instanceof Number n) return n.doubleValue();
        if (v instanceof String s) {
            try { return Double.parseDouble(s); } catch (Exception ignore) {}
        }
        return null;
    }
    private Boolean gt(Double v, double th) { return v == null ? null : (v > th); }
    private Boolean lt(Double v, double th) { return v == null ? null : (v < th); }
    private Boolean or(Boolean... arr) {
        boolean seen = false, val = false;
        for (Boolean b : arr) { if (b != null) { seen = true; val |= b; } }
        return seen ? val : null;
    }
}