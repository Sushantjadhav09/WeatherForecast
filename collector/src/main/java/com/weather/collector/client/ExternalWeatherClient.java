package com.weather.collector.client;

import com.weather.collector.dto.OpenWeatherResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class ExternalWeatherClient {

    private final WebClient webClient;
    private final String apiKey;

    public ExternalWeatherClient(
            @Value("${weather.api.base-url}") String baseUrl,
            @Value("${openweathermap.api.key}") String apiKey) {

        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();

        this.apiKey = apiKey;
    }

    public Mono<OpenWeatherResponse> getCurrentWeather(
            double latitude,
            double longitude) {

        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/data/2.5/weather")
                        .queryParam("lat", latitude)
                        .queryParam("lon", longitude)
                        .queryParam("appid", apiKey)
                        .queryParam("units", "metric")
                        .build())
                .retrieve()
                .bodyToMono(OpenWeatherResponse.class);    }
}