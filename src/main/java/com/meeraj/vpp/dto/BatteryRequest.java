package com.meeraj.vpp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BatteryRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String postcode;

    @NotBlank
    @Positive
    private Integer wattCapacity;
}
