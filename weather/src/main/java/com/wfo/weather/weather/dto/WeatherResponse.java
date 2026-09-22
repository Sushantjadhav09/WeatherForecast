package com.wfo.weather.weather.dto;

import com.wfo.weather.weather.enums.WeatherCondition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherResponse {

    private UUID id;
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

    private WeatherCondition condition;
    private String conditionDescription;

    private Instant sunriseAt;
    private Instant sunsetAt;

    private Instant recordedAt;
}
