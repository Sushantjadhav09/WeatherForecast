package com.wfo.weather.weather.service;

import com.wfo.weather.weather.dto.WeatherRequest;
import com.wfo.weather.weather.dto.WeatherResponse;
import com.wfo.weather.weather.entity.WeatherData;
import com.wfo.weather.weather.exception.WeatherNotFoundException;
import com.wfo.weather.weather.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class WeatherServiceImpl implements WeatherService {

    private final WeatherRepository weatherRepository;

    @Override
    public WeatherResponse saveWeather(
            WeatherRequest request) {

        WeatherData weatherData = WeatherData.builder()
                .locationId(request.getLocationId())
                .temperature(request.getTemperature())
                .feelsLike(request.getFeelsLike())
                .humidity(request.getHumidity())
                .pressure(request.getPressure())
                .windSpeed(request.getWindSpeed())
                .windDirection(request.getWindDirection())
                .cloudPercentage(request.getCloudPercentage())
                .visibility(request.getVisibility())
                .rainfall(request.getRainfall())
                .condition(request.getCondition())
                .conditionDescription(
                        request.getConditionDescription()
                )
                .sunriseAt(request.getSunriseAt())
                .sunsetAt(request.getSunsetAt())
                .recordedAt(request.getRecordedAt())
                .build();

        WeatherData saved =
                weatherRepository.save(weatherData);

        return mapToResponse(saved);
    }
    @Cacheable(value = "currentWeather", key = "#locationId")
    @Override
    @Transactional(readOnly = true)
    public WeatherResponse getCurrentWeather(
            UUID locationId) {
        System.out.println(">>> Fetching weather from DATABASE");

        WeatherData weatherData =
                weatherRepository
                        .findTopByLocationIdOrderByRecordedAtDesc(
                                locationId
                        )
                        .orElseThrow(
                        );

        return mapToResponse(weatherData);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WeatherResponse> getWeatherHistory(
            UUID locationId) {

        return weatherRepository
                .findByLocationIdOrderByRecordedAtDesc(
                        locationId
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private WeatherResponse mapToResponse(
            WeatherData weatherData) {

        return WeatherResponse.builder()
                .id(weatherData.getId())
                .locationId(weatherData.getLocationId())
                .temperature(weatherData.getTemperature())
                .feelsLike(weatherData.getFeelsLike())
                .humidity(weatherData.getHumidity())
                .pressure(weatherData.getPressure())
                .windSpeed(weatherData.getWindSpeed())
                .windDirection(weatherData.getWindDirection())
                .cloudPercentage(
                        weatherData.getCloudPercentage()
                )
                .visibility(weatherData.getVisibility())
                .rainfall(weatherData.getRainfall())
                .condition(weatherData.getCondition())
                .conditionDescription(
                        weatherData.getConditionDescription()
                )
                .sunriseAt(weatherData.getSunriseAt())
                .sunsetAt(weatherData.getSunsetAt())
                .recordedAt(weatherData.getRecordedAt())
                .build();
    }
}
