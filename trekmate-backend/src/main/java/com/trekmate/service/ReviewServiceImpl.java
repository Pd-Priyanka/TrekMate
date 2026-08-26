package com.trekmate.service;

import java.math.*;
import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.trekmate.dto.*;
import com.trekmate.entity.*;
import com.trekmate.exception.*;
import com.trekmate.repository.*;

@Service
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository rr;
    private final UserRepository ur;
    private final TrekRepository tr;

    public ReviewServiceImpl(ReviewRepository rr, UserRepository ur, TrekRepository tr) {
        this.rr = rr;
        this.ur = ur;
        this.tr = tr;
    }

    @Transactional
    public ReviewResponse create(String e, Long t, ReviewRequest r) {
        User u = user(e);
        Trek k = trek(t);
        if (rr.existsByUserIdAndTrekId(u.getId(), t))
            throw new ConflictException("You have already reviewed this trek.");
        Review v = new Review();
        v.setUser(u);
        v.setTrek(k);
        apply(v, r);
        return dto(rr.save(v));
    }

    public ReviewSummaryResponse list(Long t) {
        trek(t);
        List<ReviewResponse> x = rr.findAllByTrekIdOrderByCreatedAtDesc(t).stream().map(this::dto).toList();
        ReviewAggregate aggregate = rr.getAggregateByTrekId(t);
        BigDecimal average = aggregate.getAverageRating() == null ? BigDecimal.ZERO : BigDecimal.valueOf(aggregate.getAverageRating()).setScale(2, RoundingMode.HALF_UP);
        return new ReviewSummaryResponse(average, aggregate.getReviewCount(), x);
    }

    @Transactional
    public ReviewResponse update(String e, Long id, ReviewRequest r) {
        Review v = review(id);
        owner(e, v);
        apply(v, r);
        return dto(rr.save(v));
    }

    @Transactional
    public void delete(String e, Long id) {
        Review v = review(id);
        owner(e, v);
        rr.delete(v);
    }

    private void apply(Review v, ReviewRequest r) {
        v.setRating(r.rating());
        v.setComment(r.comment().trim());
    }

    private ReviewResponse dto(Review v) {
        return new ReviewResponse(v.getId(), v.getRating(), v.getComment(), v.getCreatedAt(), v.getUser().getId(), v.getUser().getName());
    }

    private User user(String e) {
        return ur.findByEmailIgnoreCase(e).orElseThrow(() -> new NotFoundException("User not found."));
    }

    private Trek trek(Long i) {
        return tr.findById(i).orElseThrow(() -> new NotFoundException("Trek not found."));
    }

    private Review review(Long i) {
        return rr.findById(i).orElseThrow(() -> new NotFoundException("Review not found."));
    }

    private void owner(String e, Review r) {
        if (!r.getUser().getEmail().equalsIgnoreCase(e))
            throw new ForbiddenException("You can modify only your own reviews.");
    }
}
