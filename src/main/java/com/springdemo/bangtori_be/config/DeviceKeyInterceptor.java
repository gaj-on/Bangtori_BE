package com.springdemo.bangtori_be.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DeviceKeyInterceptor implements HandlerInterceptor {
    private final Set<String> keys;
    public DeviceKeyInterceptor(@Value("${security.device-keys}") String csv) {
        this.keys = Arrays.stream(csv.split(",")).map(String::trim).collect(Collectors.toSet());
    }
    @Override public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object h) throws IOException {
        if (req.getRequestURI().contains("/telemetry")) {
            String key = req.getHeader("X-Device-Key");
            if (key == null || !keys.contains(key)) { res.setStatus(401); return false; }
        }
        return true;
    }
}
