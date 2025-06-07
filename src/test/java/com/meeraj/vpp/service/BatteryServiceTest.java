package com.meeraj.vpp.service;

import com.meeraj.vpp.dto.BatteryRequest;
import com.meeraj.vpp.dto.BatterySearchResponse;
import com.meeraj.vpp.entity.Battery;
import com.meeraj.vpp.repo.BatteryRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BatteryServiceTest {

    @Mock
    private BatteryRepository batteryRepository;

    @InjectMocks
    private BatteryService batteryService;

    @Test
    void test_registerBatteries() {
        List<BatteryRequest> requests = List.of(
                new BatteryRequest("Battery A", "2000", 200),
                new BatteryRequest("Battery B", "2100", 300)
        );

        when(batteryRepository.saveAll(any())).thenReturn(
                List.of(
                        new Battery("Battery A", "2000", 200),
                        new Battery("Battery B", "2100", 300)
                )
        );

        List<Battery> result = batteryService.registerBatteries(requests);

        assertEquals(2, result.size());
        verify(batteryRepository).saveAll(any());
    }

    @Test
    void test_searchBatteriesByPostcodeRange() {
        when(batteryRepository.findByPostcodeRange("2000", "7000", null, null)).thenReturn(List.of(
                new Battery("Bentley", "6102", 85000),
                new Battery("Akunda Bay", "2084", 13500),
                new Battery("Cannington", "6107", 13500)
        ));

        BatterySearchResponse response = batteryService.getBatteriesByPostcodeRange("2000", "7000", null, null);

        assertEquals(3, response.getBatteryNames().size());
        assertEquals("Akunda Bay", response.getBatteryNames().getFirst());
        assertEquals("Bentley", response.getBatteryNames().get(1));
        assertEquals("Cannington", response.getBatteryNames().get(2));
        assertEquals(112000, response.getStatistics().getTotalWattCapacity());
        assertEquals(37333.33, response.getStatistics().getAverageWattCapacity(), 0.01);
    }

    @Test
    void test_searchBatteriesByPostcodeRange_minFilter() {
        when(batteryRepository.findByPostcodeRange("2000", "7000", 20000, null))
                .thenReturn(List.of(new Battery("Bentley", "6102", 85000)));

        BatterySearchResponse response = batteryService.getBatteriesByPostcodeRange("2000", "7000", 20000, null);

        assertEquals(1, response.getBatteryNames().size());
        assertEquals("Bentley", response.getBatteryNames().getFirst());
        assertEquals(85000, response.getStatistics().getTotalWattCapacity());
        assertEquals(85000.0, response.getStatistics().getAverageWattCapacity());
    }

    @Test
    void test_searchBatteriesByPostcodeRange_maxFilter() {
        when(batteryRepository.findByPostcodeRange("2000", "7000", null, 15000))
                .thenReturn(List.of(
                        new Battery("Akunda Bay", "2084", 13500),
                        new Battery("Cannington", "6107", 13500)
                ));

        BatterySearchResponse response = batteryService.getBatteriesByPostcodeRange("2000", "7000", null, 15000);

        assertEquals(2, response.getBatteryNames().size());
        assertEquals("Akunda Bay", response.getBatteryNames().getFirst());
        assertEquals("Cannington", response.getBatteryNames().get(1));
        assertEquals(27000, response.getStatistics().getTotalWattCapacity());
        assertEquals(13500.0, response.getStatistics().getAverageWattCapacity());
    }

    @Test
    void test_searchBatteriesByPostcodeRange_bothFilter() {
        when(batteryRepository.findByPostcodeRange("2000", "7000", 13000, 14000))
                .thenReturn(List.of(
                        new Battery("Akunda Bay", "2084", 13500),
                        new Battery("Cannington", "6107", 13500)
                ));

        BatterySearchResponse response = batteryService.getBatteriesByPostcodeRange("2000", "7000", 13000, 14000);

        assertEquals(2, response.getBatteryNames().size());
        assertEquals("Akunda Bay", response.getBatteryNames().get(0));
        assertEquals("Cannington", response.getBatteryNames().get(1));
        assertEquals(27000, response.getStatistics().getTotalWattCapacity());
        assertEquals(13500.0, response.getStatistics().getAverageWattCapacity());
    }

    @Test
    void test_searchBatteriesByPostcodeRange_emptyResult() {
        BatterySearchResponse response = batteryService.getBatteriesByPostcodeRange("2000", "2010", 400, 500);

        assertTrue(response.getBatteryNames().isEmpty());
        assertEquals(0, response.getStatistics().getTotalWattCapacity());
        assertEquals(0.0, response.getStatistics().getAverageWattCapacity());
    }

}