package com.meeraj.vpp.service;

import com.meeraj.vpp.dto.BatteryRequest;
import com.meeraj.vpp.dto.BatterySearchResponse;
import com.meeraj.vpp.entity.Battery;
import com.meeraj.vpp.repo.BatteryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class BatteryService {

    private final BatteryRepository batteryRepository;

    public List<Battery> registerBatteries(List<BatteryRequest> batteryRequests) {
        log.info("Starting registering {} batteries", batteryRequests.size());

        List<Battery> batteries = batteryRequests.stream()
                .map(battery -> new Battery(battery.getName(), battery.getPostcode(), battery.getWattCapacity()))
                .toList();

        List<Battery> savedBatteries = batteryRepository.saveAll(batteries);

        log.info("Successfully registered {} batteries", savedBatteries.size());

        return savedBatteries;
    }

    public BatterySearchResponse getBatteriesByPostcodeRange(String startCode, String endCode, Integer minWattCapacity, Integer maxWattCapacity) {
        log.info("Searching batteries within postcode range {} to {}", startCode, endCode);

        List<Battery> batteries = batteryRepository.findByPostcodeRange(startCode, endCode);

        if (batteries.isEmpty()) {
            return new BatterySearchResponse(List.of(), new BatterySearchResponse.BatteryStatistics(0, 0.0));
        }

        Stream<Battery> batteryStream = batteries.stream();

        // if min watt is sent
        if (minWattCapacity != null) {
            batteryStream = batteryStream.filter(battery -> battery.getWattCapacity() >= minWattCapacity);
        }

        // if max watt is sent in the request
        if (maxWattCapacity != null) {
            batteryStream = batteryStream.filter(battery -> battery.getWattCapacity() >= maxWattCapacity);
        }

        List<Battery> filteredBatteries = batteryStream.toList();

        List<String> sortedNames = filteredBatteries.stream()
                .map(Battery::getName)
                .sorted()
                .toList();

        int totalWattCapacity = filteredBatteries.stream()
                .mapToInt(Battery::getWattCapacity)
                .sum();

        double averageWattCapacity = filteredBatteries.stream()
                .mapToInt(Battery::getWattCapacity)
                .average()
                .orElse(0.0);

        BatterySearchResponse.BatteryStatistics stats = new BatterySearchResponse.BatteryStatistics(totalWattCapacity, averageWattCapacity);

        log.info("Found {} batteries with total capacity {} watts", batteries.size(), totalWattCapacity);

        return new BatterySearchResponse(sortedNames, stats);
    }
}
