package com.weather.collector.configue;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI collectorOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("Weather Collector Service API")
                        .description("API documentation for Weather Collector Service")
                        .version("1.0.0"));
    }
}


