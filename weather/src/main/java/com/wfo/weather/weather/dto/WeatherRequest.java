package com.wfo.weather.weather.dto;

import com.wfo.weather.weather.enums.WeatherCondition;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class WeatherRequest {

    @NotNull
    private UUID locationId;

    @NotNull
    private BigDecimal temperature;

    private BigDecimal feelsLike;

    @NotNull
    private Integer humidity;

    private Integer pressure;

    private BigDecimal windSpeed;

    private Integer windDirection;

    private Integer cloudPercentage;

    private Integer visibility;

    private BigDecimal rainfall;

    @NotNull
    private WeatherCondition condition;

    private String conditionDescription;

    private Instant sunriseAt;

    private Instant sunsetAt;

    @NotNull
    private Instant recordedAt;
}
