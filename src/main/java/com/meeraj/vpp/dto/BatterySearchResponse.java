package com.meeraj.vpp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BatterySearchResponse {

    private List<String> batteryNames;
    private BatteryStatistics statistics;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    private static class BatteryStatistics {
        private int totalWattCapacity;
        private Double averageWattCapacity;
    }
}
