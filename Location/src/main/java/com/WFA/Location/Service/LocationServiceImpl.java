package com.WFA.Location.Service;

import com.WFA.Location.Dto.LocationRequest;
import com.WFA.Location.Dto.LocationResponse;
import com.WFA.Location.Entity.Location;
import com.WFA.Location.Enums.LocationStatus;
import com.WFA.Location.Exceptions.LocationNotFoundException;
import com.WFA.Location.Repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    @Override
    public LocationResponse createLocation(LocationRequest request) {

        Location location = Location.builder()
                .name(request.getName())
                .city(request.getCity())
                .state(request.getState())
                .country(request.getCountry())
                .countryCode(request.getCountryCode())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .timezone(request.getTimezone())
                .externalLocationId(request.getExternalLocationId())
                .status(LocationStatus.ACTIVE)
                .build();

        Location savedLocation = locationRepository.save(location);

        return mapToResponse(savedLocation);
    }

    @Override
    @Transactional(readOnly = true)
    public LocationResponse getLocationById(UUID id) {

        Location location = locationRepository
                .findByIdAndStatus(id, LocationStatus.ACTIVE)
                .orElseThrow(() ->
                        new LocationNotFoundException(
                                "Location not found: " + id
                        ));

        return mapToResponse(location);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocationResponse> getAllLocations() {

        return locationRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocationResponse> searchByCity(String city) {

        return locationRepository
                .findByCityIgnoreCase(city)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public LocationResponse updateLocation(
            UUID id,
            LocationRequest request) {

        Location location = locationRepository
                .findByIdAndStatus(id, LocationStatus.ACTIVE)
                .orElseThrow(() ->
                        new LocationNotFoundException(
                                "Location not found: " + id
                        ));

        location.setName(request.getName());
        location.setCity(request.getCity());
        location.setState(request.getState());
        location.setCountry(request.getCountry());
        location.setCountryCode(request.getCountryCode());
        location.setLatitude(request.getLatitude());
        location.setLongitude(request.getLongitude());
        location.setTimezone(request.getTimezone());
        location.setExternalLocationId(
                request.getExternalLocationId()
        );

        Location updatedLocation =
                locationRepository.save(location);

        return mapToResponse(updatedLocation);
    }

    @Override
    public void deactivateLocation(UUID id) {

        Location location = locationRepository
                .findById(id)
                .orElseThrow(() ->
                        new LocationNotFoundException(
                                "Location not found: " + id
                        ));

        location.setStatus(LocationStatus.INACTIVE);

        locationRepository.save(location);
    }

    private LocationResponse mapToResponse(Location location) {

        return LocationResponse.builder()
                .id(location.getId())
                .name(location.getName())
                .city(location.getCity())
                .state(location.getState())
                .country(location.getCountry())
                .countryCode(location.getCountryCode())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .timezone(location.getTimezone())
                .externalLocationId(location.getExternalLocationId())
                .status(location.getStatus())
                .createdAt(location.getCreatedAt())
                .updatedAt(location.getUpdatedAt())
                .build();
    }
}
