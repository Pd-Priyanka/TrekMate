package com.trekmate.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.trekmate.dto.TrekRequest;
import com.trekmate.dto.TrekResponse;
import com.trekmate.dto.TrekSearchCriteria;
import com.trekmate.entity.Trek;
import com.trekmate.exception.ConflictException;
import com.trekmate.exception.NotFoundException;
import com.trekmate.mapper.TrekMapper;
import com.trekmate.repository.TrekRepository;
import com.trekmate.repository.TrekSpecifications;

@Service
@Transactional(readOnly = true)
public class TrekServiceImpl implements TrekService {
    private final TrekRepository trekRepository;
    private final TrekMapper trekMapper;

    public TrekServiceImpl(TrekRepository trekRepository, TrekMapper trekMapper) {
        this.trekRepository = trekRepository;
        this.trekMapper = trekMapper;
    }

    @Override
    @Transactional
    public TrekResponse create(TrekRequest request) {
        if (trekRepository.existsBySlugIgnoreCase(request.slug()))
            throw new ConflictException("A trek with this slug already exists.");
        return trekMapper.toResponse(trekRepository.save(trekMapper.toEntity(request)));
    }

    @Override
    public Page<TrekResponse> search(TrekSearchCriteria criteria, Pageable pageable) {
        if (criteria.minDurationDays() != null && criteria.maxDurationDays() != null && criteria.minDurationDays() > criteria.maxDurationDays()) {
            throw new IllegalArgumentException("minDurationDays must not exceed maxDurationDays.");
        }
        return trekRepository.findAll(TrekSpecifications.withCriteria(criteria), pageable).map(trekMapper::toResponse);
    }

    @Override
    public TrekResponse findById(Long id) {
        return trekMapper.toResponse(getTrek(id));
    }

    @Override
    @Transactional
    public TrekResponse update(Long id, TrekRequest request) {
        if (trekRepository.existsBySlugIgnoreCaseAndIdNot(request.slug(), id))
            throw new ConflictException("A trek with this slug already exists.");
        Trek trek = getTrek(id);
        trekMapper.updateEntity(request, trek);
        return trekMapper.toResponse(trekRepository.save(trek));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        trekRepository.delete(getTrek(id));
    }

    private Trek getTrek(Long id) {
        return trekRepository.findById(id).orElseThrow(() -> new NotFoundException("Trek not found."));
    }
}
