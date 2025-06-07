package com.meeraj.vpp.controller;

import com.meeraj.vpp.dto.BatteryRequest;
import com.meeraj.vpp.dto.BatterySearchResponse;
import com.meeraj.vpp.entity.Battery;
import com.meeraj.vpp.service.BatteryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/batteries")
@RequiredArgsConstructor
public class BatteryController {

    private final BatteryService batteryService;

    @PostMapping
    public ResponseEntity<List<Battery>> registerBatteries(@Valid @RequestBody List<BatteryRequest> batteryRequests) {
        List<Battery> batteries = batteryService.registerBatteries(batteryRequests);
        return ResponseEntity.ok(batteries);
    }

    @GetMapping
    public ResponseEntity<BatterySearchResponse> getBatteriesByPostcodeRange(
            @RequestParam String startCode,
            @RequestParam String endCode,
            @RequestParam(required = false) Integer minWattCapacity,
            @RequestParam(required = false) Integer maxWattCapacity
    ){
        BatterySearchResponse response = batteryService.getBatteriesByPostcodeRange(startCode, endCode, minWattCapacity, maxWattCapacity);
        return ResponseEntity.ok(response);
    }
}
