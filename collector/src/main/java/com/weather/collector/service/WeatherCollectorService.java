package com.weather.collector.service;

import com.weather.collector.client.ExternalWeatherClient;
import com.weather.collector.client.LocationServiceClient;
import com.weather.collector.client.WeatherServiceClient;
import com.weather.collector.dto.*;
import com.weather.collector.kafka.WeatherKafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WeatherCollectorService {

    private final LocationServiceClient locationServiceClient;
    private final ExternalWeatherClient externalWeatherClient;
    private final WeatherServiceClient weatherServiceClient;
    private final WeatherConditionMapper weatherConditionMapper;
    private final WeatherKafkaProducer weatherKafkaProducer;

    public Mono<WeatherResponse> collectWeather(UUID locationId) {

        return locationServiceClient
                .getLocation(locationId)

                .flatMap(location ->
                        externalWeatherClient.getCurrentWeather(
                                        location.getLatitude().doubleValue(),
                                        location.getLongitude().doubleValue()
                                )
                                .map(weather ->
                                        buildWeatherRequest(
                                                location,
                                                weather
                                        )
                                )
                )

                .flatMap(weatherServiceClient::saveWeather)

                .flatMap(savedWeather -> {

                    WeatherEvent event = WeatherEvent.builder()
                            .locationId(savedWeather.getLocationId())
                            .temperature(savedWeather.getTemperature())
                            .humidity(savedWeather.getHumidity())
                            .condition(
                                    savedWeather.getCondition() != null
                                            ? savedWeather.getCondition().toString()
                                            : null
                            )
                            .conditionDescription(
                                    savedWeather.getConditionDescription()
                            )
                            .recordedAt(savedWeather.getRecordedAt())
                            .build();

                    return weatherKafkaProducer
                            .publishWeather(event)
                            .thenReturn(savedWeather);
                });
    }

    private WeatherRequest buildWeatherRequest(
            LocationResponse location,
            OpenWeatherResponse weather) {

        OpenWeatherResponse.Main main =
                weather.getMain();

        OpenWeatherResponse.Weather currentWeather =
                weather.getWeather().get(0);

        OpenWeatherResponse.Wind wind =
                weather.getWind();

        OpenWeatherResponse.Clouds clouds =
                weather.getClouds();

        OpenWeatherResponse.Sys sys =
                weather.getSys();

        BigDecimal rainfall = null;

        if (weather.getRain() != null) {

            if (weather.getRain().getOneHour() != null) {

                rainfall = BigDecimal.valueOf(
                        weather.getRain().getOneHour()
                );

            } else if (weather.getRain().getThreeHour() != null) {

                rainfall = BigDecimal.valueOf(
                        weather.getRain().getThreeHour()
                );
            }
        }

        return WeatherRequest.builder()

                .locationId(location.getId())

                .temperature(
                        BigDecimal.valueOf(main.getTemp())
                )

                .feelsLike(
                        main.getFeels_like() != null
                                ? BigDecimal.valueOf(main.getFeels_like())
                                : null
                )

                .humidity(main.getHumidity())

                .pressure(main.getPressure())

                .windSpeed(
                        wind != null && wind.getSpeed() != null
                                ? BigDecimal.valueOf(wind.getSpeed())
                                : null
                )

                .windDirection(
                        wind != null
                                ? wind.getDeg()
                                : null
                )

                .cloudPercentage(
                        clouds != null
                                ? clouds.getAll()
                                : null
                )

                .visibility(weather.getVisibility())

                .rainfall(rainfall)

                .condition(
                        weatherConditionMapper.map(
                                currentWeather.getMain()
                        )
                )

                .conditionDescription(
                        currentWeather.getDescription()
                )

                .sunriseAt(
                        sys != null && sys.getSunrise() != null
                                ? Instant.ofEpochSecond(sys.getSunrise())
                                : null
                )

                .sunsetAt(
                        sys != null && sys.getSunset() != null
                                ? Instant.ofEpochSecond(sys.getSunset())
                                : null
                )

                .recordedAt(Instant.now())

                .build();
    }
}
