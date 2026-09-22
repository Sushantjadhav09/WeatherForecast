package com.weather.collector.kafka;

import com.weather.collector.dto.WeatherEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class WeatherKafkaConsumer {

    @KafkaListener(
            topics = "weather-data",
            groupId = "weather-consumer-group"
    )
    public void consume(WeatherEvent event) {

        log.info("Received weather event: {}", event);
        log.info("========================================");
        log.info("Weather event received from Kafka");
        log.info("Location ID: {}", event.getLocationId());
        log.info("Temperature: {}", event.getTemperature());
        log.info("Humidity: {}", event.getHumidity());
        log.info("Condition: {}", event.getCondition());
        log.info("Recorded At: {}", event.getRecordedAt());
        log.info("========================================");
    }
}