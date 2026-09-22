package com.WFA.Location.Dto;

import com.WFA.Location.Enums.LocationStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
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
    private LocationStatus status;
    private Instant createdAt;
    private Instant updatedAt;
}