package com.trekmate.controller;

import java.util.Locale;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.trekmate.dto.TrekRequest;
import com.trekmate.dto.TrekResponse;
import com.trekmate.dto.TrekSearchCriteria;
import com.trekmate.entity.Difficulty;
import com.trekmate.service.TrekService;

@RestController
@Validated
@RequestMapping("/api/treks")
@SecurityRequirement(name = "bearerAuth")
public class TrekController {
    private static final Map<String, Sort> DEFAULT_SORTS = Map.of(
            "name", Sort.by(Sort.Direction.ASC, "name"),
            "altitude", Sort.by(Sort.Direction.DESC, "altitudeMeters"),
            "distance", Sort.by(Sort.Direction.ASC, "distanceKm"),
            "newest", Sort.by(Sort.Direction.DESC, "createdAt"));
    private final TrekService trekService;

    public TrekController(TrekService trekService) {
        this.trekService = trekService;
    }

    @PostMapping
    @Operation(summary = "Create a trek")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Trek created"), @ApiResponse(responseCode = "409", description = "Slug already exists")})
    public ResponseEntity<TrekResponse> create(@Valid @RequestBody TrekRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(trekService.create(request));
    }

    @GetMapping
    @Operation(summary = "Search treks", description = "All filters are optional and compose together. Sort options: name, altitude, distance, newest.")
    public Page<TrekResponse> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) Difficulty difficulty,
            @RequestParam(required = false) String season,
            @RequestParam(required = false) @Min(1) Integer minDurationDays,
            @RequestParam(required = false) @Min(1) Integer maxDurationDays,
            @RequestParam(defaultValue = "name") String sort,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size) {
        if (minDurationDays != null && maxDurationDays != null && minDurationDays > maxDurationDays) {
            throw new IllegalArgumentException("minDurationDays must not exceed maxDurationDays.");
        }
        TrekSearchCriteria criteria = new TrekSearchCriteria(keyword, state, difficulty, season, minDurationDays, maxDurationDays);
        return trekService.search(criteria, PageRequest.of(page, size, resolveSort(sort)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get trek by ID")
    public TrekResponse findById(@PathVariable Long id) {
        return trekService.findById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a trek")
    public TrekResponse update(@PathVariable Long id, @Valid @RequestBody TrekRequest request) {
        return trekService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a trek")
    @ApiResponse(responseCode = "204", description = "Trek deleted", content = @Content)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        trekService.delete(id);
    }

    private Sort resolveSort(String requestedSort) {
        Sort sort = DEFAULT_SORTS.get(requestedSort.trim().toLowerCase(Locale.ROOT));
        if (sort == null) throw new IllegalArgumentException("sort must be one of: name, altitude, distance, newest.");
        return sort;
    }
}
