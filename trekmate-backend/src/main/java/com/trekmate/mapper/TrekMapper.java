package com.trekmate.mapper;

import org.springframework.stereotype.Component;
import com.trekmate.dto.TrekRequest;
import com.trekmate.dto.TrekResponse;
import com.trekmate.entity.Trek;

@Component
public class TrekMapper {
    public Trek toEntity(TrekRequest request) {
        Trek trek = new Trek();
        updateEntity(request, trek);
        return trek;
    }

    public void updateEntity(TrekRequest r, Trek t) {
        t.setName(r.name().trim());
        t.setSlug(r.slug().trim().toLowerCase());
        t.setLocation(r.location().trim());
        t.setState(r.state().trim());
        t.setCountry(r.country().trim());
        t.setDifficulty(r.difficulty());
        t.setDistanceKm(r.distanceKm());
        t.setDurationDays(r.durationDays());
        t.setAltitudeMeters(r.altitudeMeters());
        t.setBestSeason(r.bestSeason().trim());
        t.setDescription(r.description().trim());
        t.setImageUrl(r.imageUrl() == null || r.imageUrl().isBlank() ? null : r.imageUrl().trim());
        t.setLatitude(r.latitude());
        t.setLongitude(r.longitude());
    }

    public TrekResponse toResponse(Trek t) {
        return new TrekResponse(t.getId(), t.getName(), t.getSlug(), t.getLocation(), t.getState(), t.getCountry(), t.getDifficulty(), t.getDistanceKm(), t.getDurationDays(), t.getAltitudeMeters(), t.getBestSeason(), t.getDescription(), t.getImageUrl(), t.getLatitude(), t.getLongitude(), t.getCreatedAt());
    }
}
