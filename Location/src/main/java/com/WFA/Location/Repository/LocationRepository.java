package com.WFA.Location.Repository;

import com.WFA.Location.Entity.Location;
import com.WFA.Location.Enums.LocationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LocationRepository
        extends JpaRepository<Location, UUID> {
    Optional<Location> findByIdAndStatus(
            UUID id,
            LocationStatus status
    );

    List<Location> findByCityIgnoreCase(String city);

    List<Location> findByCountryCodeIgnoreCase(String countryCode);

}
