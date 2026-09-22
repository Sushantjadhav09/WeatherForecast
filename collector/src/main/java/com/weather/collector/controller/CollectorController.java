package com.weather.collector.controller;

import com.weather.collector.client.ExternalWeatherClient;
import com.weather.collector.client.WeatherServiceClient;
import com.weather.collector.dto.LocationResponse;
import com.weather.collector.dto.OpenWeatherResponse;
import com.weather.collector.dto.WeatherResponse;
import com.weather.collector.client.LocationServiceClient;
import com.weather.collector.service.WeatherCollectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/collector")
@RequiredArgsConstructor
public class CollectorController {

    private final WeatherServiceClient weatherServiceClient;
    private final ExternalWeatherClient externalWeatherClient;
    private final LocationServiceClient locationServiceClient;
    private final WeatherCollectorService weatherCollectorService;

    @GetMapping("/weather-service-health")
    public Mono<String> checkWeatherServiceHealth() {

        return weatherServiceClient
                .getWeatherServiceHealth();
    }

    @GetMapping("/location/{locationId}")
    public Mono<LocationResponse> getLocation(
            @PathVariable UUID locationId) {

        return locationServiceClient
                .getLocation(locationId);
    }

    @GetMapping("/external-weather")
    public Mono<OpenWeatherResponse> getExternalWeather(
            @RequestParam double latitude,
            @RequestParam double longitude) {

        return externalWeatherClient
                .getCurrentWeather(latitude, longitude);
    }

    @GetMapping("/weather/{locationId}")
    public Mono<WeatherResponse> collectWeather(
            @PathVariable UUID locationId) {

        return weatherCollectorService
                .collectWeather(locationId);
    }
}