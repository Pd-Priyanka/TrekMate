package com.trekmate.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import com.trekmate.entity.Favorite;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Optional<Favorite> findByUserIdAndTrekId(Long userId, Long trekId);

    @EntityGraph(attributePaths = "trek")
    List<Favorite> findAllByUserIdOrderByCreatedAtDesc(Long userId);

    long countByUserId(Long userId);
}
