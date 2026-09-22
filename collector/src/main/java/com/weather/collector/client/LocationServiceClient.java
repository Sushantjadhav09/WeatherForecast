package com.weather.collector.client;


import com.weather.collector.dto.LocationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class LocationServiceClient {

    private final WebClient webClient;

    public LocationServiceClient(
            WebClient.Builder webClientBuilder,
            @Value("${location.service.base-url}") String baseUrl) {

        this.webClient = webClientBuilder
                .baseUrl(baseUrl)
                .build();
    }

    public Mono<LocationResponse> getLocation(UUID locationId) {

        return webClient
                .get()
                .uri("/api/v1/locations/{id}", locationId)
                .retrieve()
                .bodyToMono(LocationResponse.class);
    }
}
