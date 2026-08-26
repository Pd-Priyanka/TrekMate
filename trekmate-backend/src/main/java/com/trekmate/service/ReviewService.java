package com.trekmate.service;

import com.trekmate.dto.*;

public interface ReviewService {
    ReviewResponse create(String e, Long t, ReviewRequest r);

    ReviewSummaryResponse list(Long t);

    ReviewResponse update(String e, Long id, ReviewRequest r);

    void delete(String e, Long id);
}
