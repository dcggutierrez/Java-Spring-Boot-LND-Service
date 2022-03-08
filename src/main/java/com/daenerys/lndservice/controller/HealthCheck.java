package com.daenerys.lndservice.controller;

import com.daenerys.lndservice.dto.healthcheck.HealthCheckResponse;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lnd/health-check")
@Api(value = "Health Check")
public class HealthCheck {

    @GetMapping
    public HealthCheckResponse healthCheck(){
        return new HealthCheckResponse();
    }
}
