// service/AppliancePolicyService.java
package com.springdemo.bangtori_be.service;

import com.springdemo.bangtori_be.model.Appliance;
import com.springdemo.bangtori_be.model.Telemetry;
import com.springdemo.bangtori_be.repository.ApplianceRepository;
import com.springdemo.bangtori_be.repository.TelemetryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AppliancePolicyService {

    private final TelemetryRepository telemetryRepository;
    private final ApplianceRepository applianceRepository;

    public static record Averages(
            double tempAvg, double humiAvg, double pm25Avg, double tvocAvg, double co2Avg
    ) {}

    /** 최근 windowMinutes 동안 평균 계산 (단일 디바이스) */
    public Averages computeAverages(int windowMinutes) {
        long toExclusive = Instant.now().getEpochSecond();
        long from = toExclusive - windowMinutes * 60L;

        List<Telemetry> list = telemetryRepository.findRangeAsc(from, toExclusive);
        if (list == null || list.isEmpty()) {
            return new Averages(Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN);
        }

        // ✔️ 여기서 getter 문제를 회피/해결 (아래 3) 참고)
        double temp = list.stream().mapToDouble(this::getTempSafe).average().orElse(Double.NaN);
        double humi = list.stream().mapToDouble(this::getHumiSafe).average().orElse(Double.NaN);
        double pm25 = list.stream().mapToDouble(this::getPm25Safe).average().orElse(Double.NaN);
        double tvoc = list.stream().mapToDouble(this::getTvocSafe).average().orElse(Double.NaN);
        double co2  = list.stream().mapToDouble(this::getCo2Safe).average().orElse(Double.NaN);

        return new Averages(temp, humi, pm25, tvoc, co2);
    }

    /** 정책 적용 대상 상태 계산 */
    public Map<Appliance.ApplianceType, Boolean> decideDesiredStates(Averages avg,
                                                                     Map<Appliance.ApplianceType, Boolean> current) {
        Map<Appliance.ApplianceType, Boolean> desired = new EnumMap<>(Appliance.ApplianceType.class);

        boolean fanOn  = gt(avg.co2Avg(), 1000) || gt(avg.tvocAvg(), 400) || gt(avg.pm25Avg(), 75);
        boolean acOn   = gt(avg.tempAvg(), 28);
        boolean heatOn = lt(avg.tempAvg(), 18) || gt(avg.humiAvg(), 70);

        boolean robotCurrentlyOn = Boolean.TRUE.equals(current.get(Appliance.ApplianceType.ROBOT));
        boolean robotOn = gt(avg.pm25Avg(), 75) && !robotCurrentlyOn;

        desired.put(Appliance.ApplianceType.FAN,   fanOn);
        desired.put(Appliance.ApplianceType.AC,    acOn);
        desired.put(Appliance.ApplianceType.HEAT,  heatOn);
        desired.put(Appliance.ApplianceType.ROBOT, robotOn);

        return desired;
    }

    /** 계산 + 일괄 저장 + before/after 반환 */
    public ApplyResult applyPolicy(int windowMinutes) {
        Averages avg = computeAverages(windowMinutes);

        Map<Appliance.ApplianceType, Boolean> before = new EnumMap<>(Appliance.ApplianceType.class);
        Map<Appliance.ApplianceType, Appliance> entities = new EnumMap<>(Appliance.ApplianceType.class);

        for (Appliance.ApplianceType t : Appliance.ApplianceType.values()) {
            Appliance a = applianceRepository.findByType(t)
                    .orElseGet(() -> Appliance.builder().type(t).isOn(false).build());
            entities.put(t, a);
            before.put(t, a.isOn());
        }

        Map<Appliance.ApplianceType, Boolean> desired = decideDesiredStates(avg, before);

        Map<Appliance.ApplianceType, Boolean> after = new EnumMap<>(Appliance.ApplianceType.class);
        List<Appliance> toSave = new ArrayList<>();
        for (Appliance.ApplianceType t : Appliance.ApplianceType.values()) {
            Boolean want = desired.get(t);
            if (want == null) continue;
            Appliance a = entities.get(t);
            a.setOn(want);
            after.put(t, a.isOn());
            toSave.add(a);
        }
        applianceRepository.saveAll(toSave);

        return new ApplyResult(avg, before, desired, after);
    }

    public record ApplyResult(
            Averages averages,
            Map<Appliance.ApplianceType, Boolean> before,
            Map<Appliance.ApplianceType, Boolean> desired,
            Map<Appliance.ApplianceType, Boolean> after
    ) {}

    private static boolean gt(double v, double threshold) {
        return !Double.isNaN(v) && v > threshold;
    }
    private static boolean lt(double v, double threshold) {
        return !Double.isNaN(v) && v < threshold;
    }

    // ====== (3) Telemetry getter 문제 해결용 안전 접근기 ======
    private double getTempSafe(Telemetry t) { return toDouble(t.getTemp()); }
    private double getHumiSafe(Telemetry t) { return toDouble(t.getHumi()); }
    private double getPm25Safe(Telemetry t) { return toDouble(t.getDust()); } // dust를 PM2.5로 사용

    private double getTvocSafe(Telemetry t) {
        // 필드명이 tvoc, TVOC 등일 때 빌드가 깨지지 않게 하나로 모음
        try { return toDouble(t.getTvoc()); } catch (NoSuchMethodError e) { /* 아래 대안 추가 가능 */ }
        return Double.NaN;
    }
    private double getCo2Safe(Telemetry t) {
        try { return toDouble(t.getCo2()); } catch (NoSuchMethodError e) { /* 아래 대안 추가 가능 */ }
        return Double.NaN;
    }

    // Double/Integer/Long 등 wrapper 지원
    private double toDouble(Object v) {
        if (v == null) return Double.NaN;
        if (v instanceof Number n) return n.doubleValue();
        // 문자열로 들어오는 극단 케이스 방지
        try { return Double.parseDouble(v.toString()); } catch (Exception ignore) { return Double.NaN; }
    }
}
