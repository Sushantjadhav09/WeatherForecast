package com.WFA.Location.Service;

import com.WFA.Location.Dto.LocationRequest;
import com.WFA.Location.Dto.LocationResponse;

import java.util.List;
import java.util.UUID;

public interface LocationService {

    LocationResponse createLocation(LocationRequest request);

    LocationResponse getLocationById(UUID id);

    List<LocationResponse> getAllLocations();

    List<LocationResponse> searchByCity(String city);

    LocationResponse updateLocation(
            UUID id,
            LocationRequest request
    );

    void deactivateLocation(UUID id);
}