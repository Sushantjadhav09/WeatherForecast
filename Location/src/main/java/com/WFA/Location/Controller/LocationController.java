package com.WFA.Location.Controller;


import com.WFA.Location.Dto.LocationRequest;
import com.WFA.Location.Dto.LocationResponse;
import com.WFA.Location.Service.LocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @PostMapping
    public ResponseEntity<LocationResponse> createLocation(
            @Valid @RequestBody LocationRequest request) {

        LocationResponse response =
                locationService.createLocation(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<LocationResponse> getLocation(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                locationService.getLocationById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<LocationResponse>> getAllLocations() {

        return ResponseEntity.ok(
                locationService.getAllLocations()
        );
    }

    @GetMapping("/search")
    public ResponseEntity<List<LocationResponse>> searchByCity(
            @RequestParam String city) {

        return ResponseEntity.ok(
                locationService.searchByCity(city)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocationResponse> updateLocation(
            @PathVariable UUID id,
            @Valid @RequestBody LocationRequest request) {

        return ResponseEntity.ok(
                locationService.updateLocation(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateLocation(
            @PathVariable UUID id) {

        locationService.deactivateLocation(id);

        return ResponseEntity.noContent().build();
    }

}