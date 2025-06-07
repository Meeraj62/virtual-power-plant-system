package com.meeraj.vpp.service;

import com.meeraj.vpp.dto.BatteryRequest;
import com.meeraj.vpp.dto.BatterySearchResponse;
import com.meeraj.vpp.entity.Battery;
import com.meeraj.vpp.repo.BatteryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BatteryService {

    private final BatteryRepository batteryRepository;

    public List<Battery> registerBatteries(List<BatteryRequest> batteryRequests) {
        log.info("Starting registering {} batteries", batteryRequests.size());

        List<Battery> batteries = batteryRequests.stream()
                .map(battery -> new Battery(battery.getName(), battery.getPostcode(), battery.getCapacity()))
                .toList();

        List<Battery> savedBatteries = batteryRepository.saveAll(batteries);

        log.info("Successfully registered {} batteries", savedBatteries.size());

        return savedBatteries;
    }

    public BatterySearchResponse getBatteriesByPostcodeRange(String startCode, String endCode, Integer minWattCapacity, Integer maxWattCapacity) {
        log.info("Searching batteries within postcode range {} to {} with filters min={}, max={}",
                startCode, endCode, minWattCapacity, maxWattCapacity);

        List<Battery> batteries = batteryRepository.findByPostcodeRange(startCode, endCode, minWattCapacity, maxWattCapacity);

        if (batteries.isEmpty()) {
            return new BatterySearchResponse(List.of(), new BatterySearchResponse.BatteryStatistics(0, 0.0));
        }

        List<String> batteryNames = batteries.stream()
                .map(Battery::getName)
                .sorted()
                .toList();

        int totalWattCapacity = batteries.stream()
                .mapToInt(Battery::getWattCapacity)
                .sum();

        double averageWattCapacity = batteries.stream()
                .mapToInt(Battery::getWattCapacity)
                .average()
                .orElse(0.0);

        BatterySearchResponse.BatteryStatistics stats =
                new BatterySearchResponse.BatteryStatistics(totalWattCapacity, averageWattCapacity);

        log.info("Found {} batteries with total capacity {} watts", batteries.size(), totalWattCapacity);

        return new BatterySearchResponse(batteryNames, stats);
    }
}
