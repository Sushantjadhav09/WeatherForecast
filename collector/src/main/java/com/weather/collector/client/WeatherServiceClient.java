package com.weather.collector.client;

import com.weather.collector.dto.WeatherRequest;
import com.weather.collector.dto.WeatherResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class WeatherServiceClient {

    private final WebClient webClient;

    public WeatherServiceClient(
            WebClient.Builder webClientBuilder,
            @Value("${weather.service.base-url}") String baseUrl) {

        this.webClient = webClientBuilder
                .baseUrl(baseUrl)
                .build();
    }

    public Mono<WeatherResponse> saveWeather(
            WeatherRequest request) {

        return webClient
                .post()
                .uri("/api/v1/weather")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(WeatherResponse.class);
    }

    public Mono<String> getWeatherServiceHealth() {

        return webClient
                .get()
                .uri("/actuator/health")
                .retrieve()
                .bodyToMono(String.class);
    }
}