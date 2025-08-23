package com.springdemo.bangtori_be.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")

// For health check endpoint
// In case the application is running and the endpoint is accessible, it will return "pong".
// Otherwise, it will indicate that the application is not running or the endpoint is not accessible.
public class HealthController {
    @GetMapping("/ping")
    public String ping() { return "pong"; }
}
