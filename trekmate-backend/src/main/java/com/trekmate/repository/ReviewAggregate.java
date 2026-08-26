package com.trekmate.repository;

public interface ReviewAggregate {
    Double getAverageRating();

    long getReviewCount();
}
