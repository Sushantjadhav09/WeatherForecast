package com.weather.collector;

import com.weather.collector.client.ExternalWeatherClient;
import com.weather.collector.client.LocationServiceClient;
import com.weather.collector.client.WeatherServiceClient;
import com.weather.collector.dto.LocationResponse;
import com.weather.collector.dto.OpenWeatherResponse;
import com.weather.collector.dto.WeatherRequest;
import com.weather.collector.dto.WeatherResponse;
import com.weather.collector.service.WeatherCollectorService;
import com.weather.collector.service.WeatherConditionMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.mockito.Mockito.verifyNoInteractions;


@ExtendWith(MockitoExtension.class)
class CollectorApplicationTests {

	@Mock
	private LocationServiceClient locationServiceClient;

	@Mock
	private ExternalWeatherClient externalWeatherClient;

	@Mock
	private WeatherServiceClient weatherServiceClient;

	@Mock
	private WeatherConditionMapper weatherConditionMapper;

	@InjectMocks
	private WeatherCollectorService weatherCollectorService;


	@Test
	void shouldCollectWeatherSuccessfully() {

		// Arrange

		UUID locationId = UUID.randomUUID();

		LocationResponse location = new LocationResponse();
		location.setId(locationId);
		location.setName("Test Location");
		location.setLatitude(new BigDecimal("18.5204"));
		location.setLongitude(new BigDecimal("73.8567"));


		OpenWeatherResponse weather = new OpenWeatherResponse();

		OpenWeatherResponse.Main main =
				new OpenWeatherResponse.Main();

		main.setTemp(25.5);
		main.setFeels_like(24.8);
		main.setHumidity(70);
		main.setPressure(1012);

		weather.setMain(main);


		OpenWeatherResponse.Weather currentWeather =
				new OpenWeatherResponse.Weather();

		currentWeather.setMain("Clear");
		currentWeather.setDescription("clear sky");

		weather.setWeather(List.of(currentWeather));


		OpenWeatherResponse.Wind wind =
				new OpenWeatherResponse.Wind();

		wind.setSpeed(5.5);
		wind.setDeg(180);

		weather.setWind(wind);


		OpenWeatherResponse.Clouds clouds =
				new OpenWeatherResponse.Clouds();

		clouds.setAll(20);

		weather.setClouds(clouds);


		OpenWeatherResponse.Sys sys =
				new OpenWeatherResponse.Sys();

		sys.setSunrise(1757498400L);
		sys.setSunset(1757541600L);

		weather.setSys(sys);

		weather.setVisibility(10000);


		WeatherResponse savedWeather =
				new WeatherResponse();

		savedWeather.setId(UUID.randomUUID());
		savedWeather.setLocationId(locationId);
		savedWeather.setTemperature(new BigDecimal("25.5"));
		savedWeather.setFeelsLike(new BigDecimal("24.8"));
		savedWeather.setHumidity(70);
		savedWeather.setPressure(1012);
		savedWeather.setWindSpeed(new BigDecimal("5.5"));
		savedWeather.setWindDirection(180);
		savedWeather.setCloudPercentage(20);
		savedWeather.setVisibility(10000);
		savedWeather.setCondition("CLEAR");
		savedWeather.setConditionDescription("clear sky");


		when(locationServiceClient.getLocation(locationId))
				.thenReturn(Mono.just(location));

		when(externalWeatherClient.getCurrentWeather(
				18.5204,
				73.8567))
				.thenReturn(Mono.just(weather));

		when(weatherConditionMapper.map("Clear"))
				.thenReturn("CLEAR");

		when(weatherServiceClient.saveWeather(any(WeatherRequest.class)))
				.thenReturn(Mono.just(savedWeather));


		// Act

		Mono<WeatherResponse> result =
				weatherCollectorService.collectWeather(locationId);


		// Assert

		StepVerifier.create(result)
				.assertNext(response -> {

					assertEquals(
							savedWeather.getId(),
							response.getId()
					);

					assertEquals(
							locationId,
							response.getLocationId()
					);

					assertEquals(
							new BigDecimal("25.5"),
							response.getTemperature()
					);

					assertEquals(
							"CLEAR",
							response.getCondition()
					);

					assertEquals(
							"clear sky",
							response.getConditionDescription()
					);
				})
				.verifyComplete();


		// Verify

		verify(locationServiceClient)
				.getLocation(locationId);

		verify(externalWeatherClient)
				.getCurrentWeather(18.5204, 73.8567);

		verify(weatherConditionMapper)
				.map("Clear");

		verify(weatherServiceClient)
				.saveWeather(any(WeatherRequest.class));
	}

	@Test
	void shouldFailWhenLocationServiceFails() {

		// Arrange

		UUID locationId = UUID.randomUUID();

		RuntimeException exception =
				new RuntimeException("Location service unavailable");

		when(locationServiceClient.getLocation(locationId))
				.thenReturn(Mono.error(exception));


		// Act

		Mono<WeatherResponse> result =
				weatherCollectorService.collectWeather(locationId);


		// Assert

		StepVerifier.create(result)
				.expectErrorMatches(error ->
						error instanceof RuntimeException
								&& error.getMessage()
								.equals("Location service unavailable")
				)
				.verify();


		// Verify

		verify(locationServiceClient)
				.getLocation(locationId);

		verifyNoInteractions(externalWeatherClient);

		verifyNoInteractions(weatherServiceClient);

		verifyNoInteractions(weatherConditionMapper);
	}


	@Test
	void shouldFailWhenExternalWeatherApiFails() {

		// Arrange

		UUID locationId = UUID.randomUUID();

		LocationResponse location = new LocationResponse();

		location.setId(locationId);
		location.setName("Test Location");
		location.setLatitude(new BigDecimal("18.5204"));
		location.setLongitude(new BigDecimal("73.8567"));

		RuntimeException exception =
				new RuntimeException("External weather API unavailable");


		when(locationServiceClient.getLocation(locationId))
				.thenReturn(Mono.just(location));

		when(externalWeatherClient.getCurrentWeather(
				18.5204,
				73.8567))
				.thenReturn(Mono.error(exception));


		// Act

		Mono<WeatherResponse> result =
				weatherCollectorService.collectWeather(locationId);


		// Assert

		StepVerifier.create(result)
				.expectErrorMatches(error ->
						error instanceof RuntimeException
								&& error.getMessage()
								.equals("External weather API unavailable")
				)
				.verify();


		// Verify

		verify(locationServiceClient)
				.getLocation(locationId);

		verify(externalWeatherClient)
				.getCurrentWeather(18.5204, 73.8567);

		verifyNoInteractions(weatherServiceClient);

		verifyNoInteractions(weatherConditionMapper);
	}

	@Test
	void shouldMapWeatherDataAndSaveSuccessfully() {

		// Arrange

		UUID locationId = UUID.randomUUID();

		LocationResponse location = new LocationResponse();

		location.setId(locationId);
		location.setName("Test Location");
		location.setLatitude(new BigDecimal("18.5204"));
		location.setLongitude(new BigDecimal("73.8567"));


		OpenWeatherResponse weather = new OpenWeatherResponse();


		OpenWeatherResponse.Main main =
				new OpenWeatherResponse.Main();

		main.setTemp(25.5);
		main.setFeels_like(24.8);
		main.setHumidity(70);
		main.setPressure(1012);

		weather.setMain(main);


		OpenWeatherResponse.Weather currentWeather =
				new OpenWeatherResponse.Weather();

		currentWeather.setMain("Clear");
		currentWeather.setDescription("clear sky");

		weather.setWeather(List.of(currentWeather));


		OpenWeatherResponse.Wind wind =
				new OpenWeatherResponse.Wind();

		wind.setSpeed(5.5);
		wind.setDeg(180);

		weather.setWind(wind);


		OpenWeatherResponse.Clouds clouds =
				new OpenWeatherResponse.Clouds();

		clouds.setAll(20);

		weather.setClouds(clouds);


		OpenWeatherResponse.Rain rain =
				new OpenWeatherResponse.Rain();

		rain.setOneHour(2.5);

		weather.setRain(rain);


		OpenWeatherResponse.Sys sys =
				new OpenWeatherResponse.Sys();

		sys.setSunrise(1757498400L);
		sys.setSunset(1757541600L);

		weather.setSys(sys);

		weather.setVisibility(10000);


		WeatherResponse savedWeather =
				new WeatherResponse();

		savedWeather.setId(UUID.randomUUID());
		savedWeather.setLocationId(locationId);
		savedWeather.setTemperature(new BigDecimal("25.5"));
		savedWeather.setFeelsLike(new BigDecimal("24.8"));
		savedWeather.setHumidity(70);
		savedWeather.setPressure(1012);
		savedWeather.setWindSpeed(new BigDecimal("5.5"));
		savedWeather.setWindDirection(180);
		savedWeather.setCloudPercentage(20);
		savedWeather.setVisibility(10000);
		savedWeather.setRainfall(new BigDecimal("2.5"));
		savedWeather.setCondition("CLEAR");
		savedWeather.setConditionDescription("clear sky");


		when(locationServiceClient.getLocation(locationId))
				.thenReturn(Mono.just(location));

		when(externalWeatherClient.getCurrentWeather(
				18.5204,
				73.8567))
				.thenReturn(Mono.just(weather));

		when(weatherConditionMapper.map("Clear"))
				.thenReturn("CLEAR");

		when(weatherServiceClient.saveWeather(any(WeatherRequest.class)))
				.thenReturn(Mono.just(savedWeather));


		// Act

		Mono<WeatherResponse> result =
				weatherCollectorService.collectWeather(locationId);


		// Assert

		StepVerifier.create(result)
				.assertNext(response -> {

					assertEquals(
							locationId,
							response.getLocationId()
					);

					assertEquals(
							new BigDecimal("25.5"),
							response.getTemperature()
					);

					assertEquals(
							new BigDecimal("24.8"),
							response.getFeelsLike()
					);

					assertEquals(
							70,
							response.getHumidity()
					);

					assertEquals(
							1012,
							response.getPressure()
					);

					assertEquals(
							new BigDecimal("5.5"),
							response.getWindSpeed()
					);

					assertEquals(
							180,
							response.getWindDirection()
					);

					assertEquals(
							20,
							response.getCloudPercentage()
					);

					assertEquals(
							10000,
							response.getVisibility()
					);

					assertEquals(
							new BigDecimal("2.5"),
							response.getRainfall()
					);

					assertEquals(
							"CLEAR",
							response.getCondition()
					);

					assertEquals(
							"clear sky",
							response.getConditionDescription()
					);
				})
				.verifyComplete();


		// Verify

		verify(locationServiceClient)
				.getLocation(locationId);

		verify(externalWeatherClient)
				.getCurrentWeather(18.5204, 73.8567);

		verify(weatherConditionMapper)
				.map("Clear");

		verify(weatherServiceClient)
				.saveWeather(any(WeatherRequest.class));
	}


	@Test
	void shouldUseThreeHourRainfallWhenOneHourRainfallIsNotAvailable() {

		// Arrange

		UUID locationId = UUID.randomUUID();

		LocationResponse location = new LocationResponse();

		location.setId(locationId);
		location.setName("Test Location");
		location.setLatitude(new BigDecimal("18.5204"));
		location.setLongitude(new BigDecimal("73.8567"));


		OpenWeatherResponse weather =
				new OpenWeatherResponse();


		OpenWeatherResponse.Main main =
				new OpenWeatherResponse.Main();

		main.setTemp(25.5);
		main.setFeels_like(24.8);
		main.setHumidity(70);
		main.setPressure(1012);

		weather.setMain(main);


		OpenWeatherResponse.Weather currentWeather =
				new OpenWeatherResponse.Weather();

		currentWeather.setMain("Rain");
		currentWeather.setDescription("light rain");

		weather.setWeather(List.of(currentWeather));


		OpenWeatherResponse.Rain rain =
				new OpenWeatherResponse.Rain();

		// 1-hour rainfall is not available
		rain.setOneHour(null);

		// 3-hour rainfall is available
		rain.setThreeHour(7.5);

		weather.setRain(rain);


		WeatherResponse savedWeather =
				new WeatherResponse();

		savedWeather.setId(UUID.randomUUID());
		savedWeather.setLocationId(locationId);
		savedWeather.setTemperature(new BigDecimal("25.5"));
		savedWeather.setCondition("RAIN");
		savedWeather.setConditionDescription("light rain");
		savedWeather.setRainfall(new BigDecimal("7.5"));


		when(locationServiceClient.getLocation(locationId))
				.thenReturn(Mono.just(location));

		when(externalWeatherClient.getCurrentWeather(
				18.5204,
				73.8567))
				.thenReturn(Mono.just(weather));

		when(weatherConditionMapper.map("Rain"))
				.thenReturn("RAIN");

		when(weatherServiceClient.saveWeather(any(WeatherRequest.class)))
				.thenReturn(Mono.just(savedWeather));


		// Act

		Mono<WeatherResponse> result =
				weatherCollectorService.collectWeather(locationId);


		// Assert

		StepVerifier.create(result)
				.assertNext(response -> {

					assertEquals(
							new BigDecimal("7.5"),
							response.getRainfall()
					);

					assertEquals(
							"RAIN",
							response.getCondition()
					);

					assertEquals(
							"light rain",
							response.getConditionDescription()
					);
				})
				.verifyComplete();


		// Verify

		verify(locationServiceClient)
				.getLocation(locationId);

		verify(externalWeatherClient)
				.getCurrentWeather(18.5204, 73.8567);

		verify(weatherConditionMapper)
				.map("Rain");

		verify(weatherServiceClient)
				.saveWeather(any(WeatherRequest.class));
	}
 }


