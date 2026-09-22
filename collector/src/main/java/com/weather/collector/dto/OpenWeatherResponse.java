package com.weather.collector.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OpenWeatherResponse {

    private Main main;
    private List<Weather> weather;
    private Wind wind;
    private Clouds clouds;
    private Sys sys;
    private Integer visibility;
    private Rain rain;

    @Getter
    @Setter
    public static class Main {
        private Double temp;
        private Double feels_like;
        private Integer pressure;
        private Integer humidity;
    }

    @Getter
    @Setter
    public static class Weather {
        private String main;
        private String description;
    }

    @Getter
    @Setter
    public static class Wind {
        private Double speed;
        private Integer deg;
    }

    @Getter
    @Setter
    public static class Clouds {
        private Integer all;
    }

    @Getter
    @Setter
    public static class Sys {
        private Long sunrise;
        private Long sunset;
    }
    @Getter
    @Setter
    public static class Rain {

        @JsonProperty("1h")
        private Double oneHour;

        @JsonProperty("3h")
        private Double threeHour;
    }
}
