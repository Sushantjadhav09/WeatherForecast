package com.wfo.weather.weather.service;

import com.wfo.weather.weather.dto.WeatherRequest;
import com.wfo.weather.weather.dto.WeatherResponse;

import java.util.List;
import java.util.UUID;

public interface WeatherService {

    WeatherResponse getCurrentWeather(UUID locationId);

    List<WeatherResponse> getWeatherHistory(UUID locationId);

    WeatherResponse saveWeather(WeatherRequest request);
}
