package com.wfo.weather.weather;

import com.wfo.weather.weather.repository.WeatherRepository;
import com.wfo.weather.weather.service.WeatherServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import com.wfo.weather.weather.dto.WeatherRequest;
import com.wfo.weather.weather.dto.WeatherResponse;
import com.wfo.weather.weather.entity.WeatherData;
import com.wfo.weather.weather.enums.WeatherCondition;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WeatherApplicationTests {

	@Mock
	private WeatherRepository weatherRepository;

	@InjectMocks
	private WeatherServiceImpl weatherServiceImpl;


	@Test
	void shouldSaveWeatherSuccessfully() {

		// Arrange

		UUID locationId = UUID.randomUUID();

		Instant recordedAt = Instant.parse("2026-09-10T10:00:00Z");

		WeatherRequest request = new WeatherRequest();

		request.setLocationId(locationId);
		request.setTemperature(new BigDecimal("25.50"));
		request.setFeelsLike(new BigDecimal("26.20"));
		request.setHumidity(70);
		request.setPressure(1012);
		request.setWindSpeed(new BigDecimal("5.50"));
		request.setWindDirection(180);
		request.setCloudPercentage(40);
		request.setVisibility(10000);
		request.setRainfall(new BigDecimal("0.00"));
		request.setCondition(WeatherCondition.CLEAR);
		request.setConditionDescription("Clear sky");
		request.setSunriseAt(
				Instant.parse("2026-09-10T00:30:00Z")
		);
		request.setSunsetAt(
				Instant.parse("2026-09-10T13:00:00Z")
		);
		request.setRecordedAt(recordedAt);


		UUID weatherId = UUID.randomUUID();

		WeatherData savedWeather = WeatherData.builder()
				.id(weatherId)
				.locationId(locationId)
				.temperature(new BigDecimal("25.50"))
				.feelsLike(new BigDecimal("26.20"))
				.humidity(70)
				.pressure(1012)
				.windSpeed(new BigDecimal("5.50"))
				.windDirection(180)
				.cloudPercentage(40)
				.visibility(10000)
				.rainfall(new BigDecimal("0.00"))
				.condition(WeatherCondition.CLEAR)
				.conditionDescription("Clear sky")
				.sunriseAt(
						Instant.parse("2026-09-10T00:30:00Z")
				)
				.sunsetAt(
						Instant.parse("2026-09-10T13:00:00Z")
				)
				.recordedAt(recordedAt)
				.build();


		when(weatherRepository.save(any(WeatherData.class)))
				.thenReturn(savedWeather);


		// Act

		WeatherResponse response =
				weatherServiceImpl.saveWeather(request);


		// Assert

		assertNotNull(response);

		assertEquals(weatherId, response.getId());

		assertEquals(locationId, response.getLocationId());

		assertEquals(
				new BigDecimal("25.50"),
				response.getTemperature()
		);

		assertEquals(
				new BigDecimal("26.20"),
				response.getFeelsLike()
		);

		assertEquals(70, response.getHumidity());

		assertEquals(
				WeatherCondition.CLEAR,
				response.getCondition()
		);

		assertEquals(
				"Clear sky",
				response.getConditionDescription()
		);

		assertEquals(recordedAt, response.getRecordedAt());


		// Verify

		verify(weatherRepository)
				.save(any(WeatherData.class));
	}

	@Test
	void shouldGetCurrentWeatherSuccessfully() {

		// Arrange

		UUID locationId = UUID.randomUUID();

		UUID weatherId = UUID.randomUUID();

		Instant recordedAt =
				Instant.parse("2026-09-10T10:00:00Z");

		WeatherData weatherData = WeatherData.builder()
				.id(weatherId)
				.locationId(locationId)
				.temperature(new BigDecimal("25.50"))
				.feelsLike(new BigDecimal("26.20"))
				.humidity(70)
				.pressure(1012)
				.windSpeed(new BigDecimal("5.50"))
				.windDirection(180)
				.cloudPercentage(40)
				.visibility(10000)
				.rainfall(new BigDecimal("0.00"))
				.condition(WeatherCondition.CLEAR)
				.conditionDescription("Clear sky")
				.sunriseAt(
						Instant.parse("2026-09-10T00:30:00Z")
				)
				.sunsetAt(
						Instant.parse("2026-09-10T13:00:00Z")
				)
				.recordedAt(recordedAt)
				.build();


		when(weatherRepository
				.findTopByLocationIdOrderByRecordedAtDesc(locationId))
				.thenReturn(java.util.Optional.of(weatherData));


		// Act

		WeatherResponse response =
				weatherServiceImpl.getCurrentWeather(locationId);


		// Assert

		assertNotNull(response);

		assertEquals(weatherId, response.getId());

		assertEquals(locationId, response.getLocationId());

		assertEquals(
				new BigDecimal("25.50"),
				response.getTemperature()
		);

		assertEquals(
				new BigDecimal("26.20"),
				response.getFeelsLike()
		);

		assertEquals(70, response.getHumidity());

		assertEquals(
				WeatherCondition.CLEAR,
				response.getCondition()
		);

		assertEquals(
				"Clear sky",
				response.getConditionDescription()
		);

		assertEquals(
				recordedAt,
				response.getRecordedAt()
		);


		// Verify

		verify(weatherRepository)
				.findTopByLocationIdOrderByRecordedAtDesc(locationId);
	}

	@Test
	void shouldGetWeatherHistorySuccessfully() {

		// Arrange

		UUID locationId = UUID.randomUUID();

		WeatherData weather1 = WeatherData.builder()
				.id(UUID.randomUUID())
				.locationId(locationId)
				.temperature(new BigDecimal("25.50"))
				.humidity(70)
				.condition(WeatherCondition.CLEAR)
				.conditionDescription("Clear sky")
				.recordedAt(Instant.parse("2026-09-10T10:00:00Z"))
				.build();

		WeatherData weather2 = WeatherData.builder()
				.id(UUID.randomUUID())
				.locationId(locationId)
				.temperature(new BigDecimal("23.50"))
				.humidity(75)
				.condition(WeatherCondition.CLEAR)
				.conditionDescription("Partly cloudy")
				.recordedAt(Instant.parse("2026-09-10T08:00:00Z"))
				.build();


		when(weatherRepository
				.findByLocationIdOrderByRecordedAtDesc(locationId))
				.thenReturn(java.util.List.of(weather1, weather2));


		// Act

		java.util.List<WeatherResponse> response =
				weatherServiceImpl.getWeatherHistory(locationId);


		// Assert

		assertNotNull(response);

		assertEquals(2, response.size());

		assertEquals(
				weather1.getId(),
				response.get(0).getId()
		);

		assertEquals(
				new BigDecimal("25.50"),
				response.get(0).getTemperature()
		);

		assertEquals(
				weather2.getId(),
				response.get(1).getId()
		);

		assertEquals(
				new BigDecimal("23.50"),
				response.get(1).getTemperature()
		);


		// Verify

		verify(weatherRepository)
				.findByLocationIdOrderByRecordedAtDesc(locationId);
	}

	@Test
	void shouldReturnEmptyWeatherHistoryWhenNoWeatherFound() {

		// Arrange

		UUID locationId = UUID.randomUUID();

		when(weatherRepository
				.findByLocationIdOrderByRecordedAtDesc(locationId))
				.thenReturn(List.of());


		// Act

		List<WeatherResponse> response =
				weatherServiceImpl.getWeatherHistory(locationId);


		// Assert

		assertNotNull(response);

		assertTrue(response.isEmpty());


		// Verify

		verify(weatherRepository)
				.findByLocationIdOrderByRecordedAtDesc(locationId);
	}

}
