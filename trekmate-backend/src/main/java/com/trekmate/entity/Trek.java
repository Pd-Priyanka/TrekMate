package com.trekmate.entity;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "treks")
@Getter
@Setter
@NoArgsConstructor
public class Trek {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 150)
    private String name;
    @Column(nullable = false, unique = true, length = 180)
    private String slug;
    @Column(nullable = false, length = 150)
    private String location;
    @Column(nullable = false, length = 100)
    private String state;
    @Column(nullable = false, length = 100)
    private String country;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Difficulty difficulty;
    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal distanceKm;
    @Column(nullable = false)
    private Integer durationDays;
    @Column(nullable = false)
    private Integer altitudeMeters;
    @Column(nullable = false, length = 100)
    private String bestSeason;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;
    @Column(length = 500)
    private String imageUrl;
    @Column(nullable = false, precision = 10, scale = 7)
    private BigDecimal latitude;
    @Column(nullable = false, precision = 10, scale = 7)
    private BigDecimal longitude;
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    void initializeCreatedAt() {
        if (createdAt == null) createdAt = Instant.now();
    }
}
