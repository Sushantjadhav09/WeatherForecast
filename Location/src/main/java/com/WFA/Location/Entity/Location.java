package com.WFA.Location.Entity;

import com.WFA.Location.Enums.LocationStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;



    @Entity
    @Table(name = "locations")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class Location {

        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID id;

        @Column(nullable = false)
        private String name;

        @Column(nullable = false)
        private String city;

        private String state;

        @Column(nullable = false)
        private String country;

        @Column(name = "country_code", length = 2)
        private String countryCode;

        @Column(nullable = false)
        private BigDecimal latitude;

        @Column(nullable = false)
        private BigDecimal longitude;

        private String timezone;

        @Column(name = "external_location_id")
        private String externalLocationId;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private LocationStatus status;

        @CreationTimestamp
        @Column(name = "created_at", nullable = false, updatable = false)
        private Instant createdAt;

        @UpdateTimestamp
        @Column(name = "updated_at")
        private Instant updatedAt;
    }

