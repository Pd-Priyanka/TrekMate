package com.trekmate.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.trekmate.dto.TrekRequest;
import com.trekmate.dto.TrekResponse;
import com.trekmate.dto.TrekSearchCriteria;

public interface TrekService {
    TrekResponse create(TrekRequest request);

    Page<TrekResponse> search(TrekSearchCriteria criteria, Pageable pageable);

    TrekResponse findById(Long id);

    TrekResponse update(Long id, TrekRequest request);

    void delete(Long id);
}
