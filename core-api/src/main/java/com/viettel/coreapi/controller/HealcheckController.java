package com.viettel.coreapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/actuator/")
public class HealcheckController {

    @GetMapping({"/health", "/health/liveness", "/health/readiness"})
    public Map<String, String> check() {
        return Map.of("status", "UP");
    }

}
