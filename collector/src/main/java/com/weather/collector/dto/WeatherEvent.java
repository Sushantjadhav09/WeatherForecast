package com.weather.collector.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherEvent {

    private UUID locationId;

    private BigDecimal temperature;

    private Integer humidity;

    private String condition;

    private String conditionDescription;

    private Instant recordedAt;
}