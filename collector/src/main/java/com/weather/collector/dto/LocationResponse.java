package com.weather.collector.dto;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class LocationResponse {

    private UUID id;

    private String name;

    private String city;

    private String state;

    private String country;

    private String countryCode;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private String timezone;

    private String externalLocationId;

    private String status;
}
