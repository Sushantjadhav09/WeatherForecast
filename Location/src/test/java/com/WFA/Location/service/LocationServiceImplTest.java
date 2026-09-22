package com.WFA.Location.service;

import com.WFA.Location.Repository.LocationRepository;
import com.WFA.Location.Service.LocationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.WFA.Location.Dto.LocationRequest;
import com.WFA.Location.Dto.LocationResponse;
import com.WFA.Location.Entity.Location;
import com.WFA.Location.Enums.LocationStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.ArgumentMatchers.any;

import com.WFA.Location.Exceptions.LocationNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocationServiceImplTest {

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private LocationServiceImpl locationService;

    @Test
    void shouldCreateLocationSuccessfully() {

        // Arrange
        UUID locationId = UUID.randomUUID();

        LocationRequest request = new LocationRequest();

        request.setName("Pune");
        request.setCity("Pune");
        request.setState("Maharashtra");
        request.setCountry("India");
        request.setCountryCode("IN");
        request.setLatitude(new BigDecimal("18.5204"));
        request.setLongitude(new BigDecimal("73.8567"));
        request.setTimezone("Asia/Kolkata");
        request.setExternalLocationId("PUNE-001");


        Location savedLocation = Location.builder()
                .id(locationId)
                .name("Pune")
                .city("Pune")
                .state("Maharashtra")
                .country("India")
                .countryCode("IN")
                .latitude(new BigDecimal("18.5204"))
                .longitude(new BigDecimal("73.8567"))
                .timezone("Asia/Kolkata")
                .externalLocationId("PUNE-001")
                .status(LocationStatus.ACTIVE)
                .build();


        when(locationRepository.save(any(Location.class)))
                .thenReturn(savedLocation);


        // Act
        LocationResponse response =
                locationService.createLocation(request);


        // Assert
        assertNotNull(response);
        assertEquals(locationId, response.getId());
        assertEquals("Pune", response.getName());
        assertEquals("Pune", response.getCity());
        assertEquals(LocationStatus.ACTIVE, response.getStatus());


        // Verify
        verify(locationRepository)
                .save(any(Location.class));
    }
    @Test
    void shouldThrowExceptionWhenLocationDoesNotExist() {

        // Arrange
        UUID locationId = UUID.randomUUID();

        when(locationRepository.findByIdAndStatus(
                locationId,
                LocationStatus.ACTIVE
        )).thenReturn(Optional.empty());


        // Act + Assert
        LocationNotFoundException exception =
                assertThrows(
                        LocationNotFoundException.class,
                        () -> locationService.getLocationById(locationId)
                );


        // Verify
        assertEquals(
                "Location not found: " + locationId,
                exception.getMessage()
        );

        verify(locationRepository)
                .findByIdAndStatus(
                        locationId,
                        LocationStatus.ACTIVE
                );
    }

    @Test
    void shouldReturnAllLocations() {

        // Arrange
        Location location1 = Location.builder()
                .id(UUID.randomUUID())
                .name("Pune")
                .city("Pune")
                .state("Maharashtra")
                .country("India")
                .countryCode("IN")
                .latitude(new BigDecimal("18.5204"))
                .longitude(new BigDecimal("73.8567"))
                .status(LocationStatus.ACTIVE)
                .build();

        Location location2 = Location.builder()
                .id(UUID.randomUUID())
                .name("Mumbai")
                .city("Mumbai")
                .state("Maharashtra")
                .country("India")
                .countryCode("IN")
                .latitude(new BigDecimal("19.0760"))
                .longitude(new BigDecimal("72.8777"))
                .status(LocationStatus.ACTIVE)
                .build();


        when(locationRepository.findAll())
                .thenReturn(List.of(location1, location2));


        // Act
        List<LocationResponse> response =
                locationService.getAllLocations();


        // Assert
        assertNotNull(response);

        assertEquals(2, response.size());

        assertEquals("Pune", response.get(0).getCity());

        assertEquals("Mumbai", response.get(1).getCity());


        // Verify
        verify(locationRepository).findAll();
    }
    @Test
    void shouldSearchLocationsByCitySuccessfully() {

        // Arrange

        Location pune = Location.builder()
                .id(UUID.randomUUID())
                .name("Pune Location")
                .city("Pune")
                .state("Maharashtra")
                .country("India")
                .countryCode("IN")
                .latitude(new BigDecimal("18.5204"))
                .longitude(new BigDecimal("73.8567"))
                .timezone("Asia/Kolkata")
                .status(LocationStatus.ACTIVE)
                .build();


        when(locationRepository.findByCityIgnoreCase("Pune"))
                .thenReturn(List.of(pune));


        // Act

        List<LocationResponse> response =
                locationService.searchByCity("Pune");


        // Assert

        assertNotNull(response);

        assertEquals(1, response.size());

        assertEquals("Pune", response.get(0).getCity());

        assertEquals(
                "Pune Location",
                response.get(0).getName()
        );


        // Verify

        verify(locationRepository)
                .findByCityIgnoreCase("Pune");
    }
    @Test
    void shouldUpdateLocationSuccessfully() {

        // Arrange

        UUID locationId = UUID.randomUUID();

        Location existingLocation = Location.builder()
                .id(locationId)
                .name("Old Pune Location")
                .city("Pune")
                .state("Maharashtra")
                .country("India")
                .countryCode("IN")
                .latitude(new BigDecimal("18.5204"))
                .longitude(new BigDecimal("73.8567"))
                .timezone("Asia/Kolkata")
                .status(LocationStatus.ACTIVE)
                .build();


        LocationRequest request = new LocationRequest();

        request.setName("Updated Pune Location");
        request.setCity("Pune");
        request.setState("Maharashtra");
        request.setCountry("India");
        request.setCountryCode("IN");
        request.setLatitude(new BigDecimal("18.5304"));
        request.setLongitude(new BigDecimal("73.8667"));
        request.setTimezone("Asia/Kolkata");
        request.setExternalLocationId(null);


        when(locationRepository.findByIdAndStatus(
                locationId,
                LocationStatus.ACTIVE
        )).thenReturn(java.util.Optional.of(existingLocation));


        when(locationRepository.save(existingLocation))
                .thenReturn(existingLocation);


        // Act

        LocationResponse response =
                locationService.updateLocation(
                        locationId,
                        request
                );


        // Assert

        assertNotNull(response);

        assertEquals(
                "Updated Pune Location",
                response.getName()
        );

        assertEquals(
                "Pune",
                response.getCity()
        );

        assertEquals(
                new BigDecimal("18.5304"),
                response.getLatitude()
        );

        assertEquals(
                new BigDecimal("73.8667"),
                response.getLongitude()
        );

        assertEquals(
                LocationStatus.ACTIVE,
                response.getStatus()
        );


        // Verify

        verify(locationRepository)
                .findByIdAndStatus(
                        locationId,
                        LocationStatus.ACTIVE
                );

        verify(locationRepository)
                .save(existingLocation);
    }
    @Test
    void shouldThrowExceptionWhenUpdatingLocationDoesNotExist() {

        // Arrange

        UUID locationId = UUID.randomUUID();

        LocationRequest request = new LocationRequest();

        request.setName("Updated Pune Location");
        request.setCity("Pune");
        request.setState("Maharashtra");
        request.setCountry("India");
        request.setCountryCode("IN");
        request.setLatitude(new BigDecimal("18.5304"));
        request.setLongitude(new BigDecimal("73.8667"));
        request.setTimezone("Asia/Kolkata");


        when(locationRepository.findByIdAndStatus(
                locationId,
                LocationStatus.ACTIVE
        )).thenReturn(java.util.Optional.empty());


        // Act & Assert

        assertThrows(
                LocationNotFoundException.class,
                () -> locationService.updateLocation(
                        locationId,
                        request
                )
        );


        // Verify

        verify(locationRepository)
                .findByIdAndStatus(
                        locationId,
                        LocationStatus.ACTIVE
                );

        verify(locationRepository, never())
                .save(any(Location.class));
    }

    @Test
    void shouldDeactivateLocationSuccessfully() {

        // Arrange

        UUID locationId = UUID.randomUUID();

        Location location = Location.builder()
                .id(locationId)
                .name("Pune Location")
                .city("Pune")
                .state("Maharashtra")
                .country("India")
                .countryCode("IN")
                .latitude(new BigDecimal("18.5204"))
                .longitude(new BigDecimal("73.8567"))
                .timezone("Asia/Kolkata")
                .status(LocationStatus.ACTIVE)
                .build();


        when(locationRepository.findById(locationId))
                .thenReturn(java.util.Optional.of(location));


        // Act

        locationService.deactivateLocation(locationId);


        // Assert

        assertEquals(
                LocationStatus.INACTIVE,
                location.getStatus()
        );


        // Verify

        verify(locationRepository)
                .findById(locationId);

        verify(locationRepository)
                .save(location);
    }
    @Test
    void shouldThrowExceptionWhenDeactivatingLocationDoesNotExist() {

        // Arrange

        UUID locationId = UUID.randomUUID();


        when(locationRepository.findById(locationId))
                .thenReturn(java.util.Optional.empty());


        // Act & Assert

        assertThrows(
                LocationNotFoundException.class,
                () -> locationService.deactivateLocation(locationId)
        );


        // Verify

        verify(locationRepository)
                .findById(locationId);

        verify(locationRepository, never())
                .save(any(Location.class));
    }
}