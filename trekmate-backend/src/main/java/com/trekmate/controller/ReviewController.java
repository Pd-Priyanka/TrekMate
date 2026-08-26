package com.trekmate.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.trekmate.dto.*;
import com.trekmate.service.ReviewService;

@RestController
@SecurityRequirement(name = "bearerAuth")
public class ReviewController {
    private final ReviewService service;

    public ReviewController(ReviewService service) {
        this.service = service;
    }

    @PostMapping("/api/treks/{trekId}/reviews")
    public ResponseEntity<ReviewResponse> create(@AuthenticationPrincipal UserDetails u, @PathVariable Long trekId, @Valid @RequestBody ReviewRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(u.getUsername(), trekId, r));
    }

    @GetMapping("/api/treks/{trekId}/reviews")
    public ReviewSummaryResponse list(@PathVariable Long trekId) {
        return service.list(trekId);
    }

    @PutMapping("/api/reviews/{id}")
    public ReviewResponse update(@AuthenticationPrincipal UserDetails u, @PathVariable Long id, @Valid @RequestBody ReviewRequest r) {
        return service.update(u.getUsername(), id, r);
    }

    @DeleteMapping("/api/reviews/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal UserDetails u, @PathVariable Long id) {
        service.delete(u.getUsername(), id);
    }
}
