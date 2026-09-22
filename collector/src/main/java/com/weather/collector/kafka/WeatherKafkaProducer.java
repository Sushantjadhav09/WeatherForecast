package com.weather.collector.kafka;

import com.weather.collector.dto.WeatherEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class WeatherKafkaProducer {

    private static final String TOPIC = "weather-data";

    private final KafkaTemplate<String, WeatherEvent> kafkaTemplate;

    public Mono<Void> publishWeather(WeatherEvent event) {

        return Mono.fromFuture(
                kafkaTemplate.send(
                        TOPIC,
                        event.getLocationId().toString(),
                        event
                )
        ).then();
    }
}
