package com.trekmate.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.trekmate.dto.FavoriteCountResponse;
import com.trekmate.dto.FavoriteListResponse;
import com.trekmate.dto.FavoriteMutationResponse;
import com.trekmate.service.FavoriteService;

@RestController
@RequestMapping("/api/favorites")
@SecurityRequirement(name = "bearerAuth")
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/{trekId}")
    @Operation(summary = "Add a trek to the authenticated user's favorites")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Favorite added"), @ApiResponse(responseCode = "409", description = "Already a favorite")})
    public ResponseEntity<FavoriteMutationResponse> add(@AuthenticationPrincipal UserDetails user, @PathVariable Long trekId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(favoriteService.add(user.getUsername(), trekId));
    }

    @DeleteMapping("/{trekId}")
    @Operation(summary = "Remove a trek from the authenticated user's favorites")
    public FavoriteCountResponse remove(@AuthenticationPrincipal UserDetails user, @PathVariable Long trekId) {
        return favoriteService.remove(user.getUsername(), trekId);
    }

    @GetMapping
    @Operation(summary = "Get the authenticated user's favorites, including favorite count")
    public FavoriteListResponse findAll(@AuthenticationPrincipal UserDetails user) {
        return favoriteService.findAll(user.getUsername());
    }
}
