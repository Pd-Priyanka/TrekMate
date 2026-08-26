package com.trekmate.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.trekmate.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByUserIdAndTrekId(Long u, Long t);

    @EntityGraph(attributePaths = "user")
    List<Review> findAllByTrekIdOrderByCreatedAtDesc(Long t);

    @Query("select avg(r.rating) as averageRating, count(r) as reviewCount from Review r where r.trek.id = :trekId")
    ReviewAggregate getAggregateByTrekId(@Param("trekId") Long trekId);
}
