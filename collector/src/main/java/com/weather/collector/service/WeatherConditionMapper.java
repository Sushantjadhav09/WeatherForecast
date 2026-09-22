package com.weather.collector.service;

import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class WeatherConditionMapper {

    public String map(String condition) {

        if (condition == null) {
            return "OTHER";
        }

        return switch (condition.toUpperCase(Locale.ROOT)) {

            case "CLEAR" -> "CLEAR";
            case "CLOUDS" -> "CLOUDS";
            case "RAIN" -> "RAIN";
            case "DRIZZLE" -> "DRIZZLE";
            case "THUNDERSTORM" -> "THUNDERSTORM";
            case "SNOW" -> "SNOW";
            case "MIST" -> "MIST";
            case "FOG" -> "FOG";
            case "HAZE" -> "HAZE";
            case "DUST" -> "DUST";
            case "SMOKE" -> "SMOKE";

            default -> "OTHER";
        };
    }
}
