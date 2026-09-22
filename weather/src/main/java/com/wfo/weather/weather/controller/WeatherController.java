package com.wfo.weather.weather.controller;

import com.wfo.weather.weather.dto.WeatherRequest;
import com.wfo.weather.weather.dto.WeatherResponse;
import com.wfo.weather.weather.service.WeatherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping("/current/{locationId}")
    public ResponseEntity<WeatherResponse> getCurrentWeather(
            @PathVariable UUID locationId) {

        return ResponseEntity.ok(
                weatherService.getCurrentWeather(locationId)
        );
    }

    @GetMapping("/history/{locationId}")
    public ResponseEntity<List<WeatherResponse>> getWeatherHistory(
            @PathVariable UUID locationId) {

        return ResponseEntity.ok(
                weatherService.getWeatherHistory(locationId)
        );
    }

    @PostMapping
    public ResponseEntity<WeatherResponse> saveWeather(
            @Valid @RequestBody WeatherRequest request) {

        WeatherResponse response =
                weatherService.saveWeather(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
