package com.weather.collector.dto;


import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class WeatherRequest {

    private UUID locationId;

    private BigDecimal temperature;

    private BigDecimal feelsLike;

    private Integer humidity;

    private Integer pressure;

    private BigDecimal windSpeed;

    private Integer windDirection;

    private Integer cloudPercentage;

    private Integer visibility;

    private BigDecimal rainfall;

    private String condition;

    private String conditionDescription;

    private Instant sunriseAt;

    private Instant sunsetAt;

    private Instant recordedAt;
}
