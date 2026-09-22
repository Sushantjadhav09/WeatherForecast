package com.WFA.Location.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class LocationRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String city;

    private String state;

    @NotBlank
    private String country;

    @Size(min = 2, max = 2)
    private String countryCode;

    @NotNull
    private BigDecimal latitude;

    @NotNull
    private BigDecimal longitude;

    private String timezone;

    private String externalLocationId;
}