package com.wfo.weather.weather.entity;

import com.wfo.weather.weather.enums.WeatherCondition;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "weather_data",
        indexes = {
                @Index(
                        name = "idx_weather_location_recorded",
                        columnList = "location_id, recorded_at"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherData {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "location_id", nullable = false)
    private UUID locationId;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal temperature;

    @Column(name = "feels_like", precision = 5, scale = 2)
    private BigDecimal feelsLike;

    @Column(nullable = false)
    private Integer humidity;

    @Column
    private Integer pressure;

    @Column(name = "wind_speed", precision = 6, scale = 2)
    private BigDecimal windSpeed;

    @Column(name = "wind_direction")
    private Integer windDirection;

    @Column(name = "cloud_percentage")
    private Integer cloudPercentage;

    @Column
    private Integer visibility;

    @Column(name = "rainfall", precision = 6, scale = 2)
    private BigDecimal rainfall;

    @Enumerated(EnumType.STRING)
    @Column(name = "weather_condition", nullable = false)
    private WeatherCondition condition;

    @Column(name = "condition_description")
    private String conditionDescription;

    @Column(name = "sunrise_at")
    private Instant sunriseAt;

    @Column(name = "sunset_at")
    private Instant sunsetAt;

    @Column(name = "recorded_at", nullable = false)
    private Instant recordedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}
