package com.trekmate.entity;

import java.time.Instant;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "favorites", uniqueConstraints = @UniqueConstraint(name = "uk_favorites_user_trek", columnNames = {"user_id", "trek_id"}))
@Getter
@Setter
@NoArgsConstructor
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "trek_id", nullable = false)
    private Trek trek;
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    void initializeCreatedAt() {
        if (createdAt == null) createdAt = Instant.now();
    }
}
