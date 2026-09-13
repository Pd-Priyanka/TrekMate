package com.trekmate.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.trekmate.dto.WeatherResponse;
import com.trekmate.entity.Trek;
import com.trekmate.exception.NotFoundException;
import com.trekmate.repository.TrekRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class OpenWeatherService implements WeatherService {
    private static final Duration CACHE_DURATION = Duration.ofMinutes(15);
    private static final int REQUEST_TIMEOUT_MILLIS = 5_000;

    private final TrekRepository treks;
    private final RestClient client;
    private final String key;
    private final ConcurrentMap<Long, Cached> cache = new ConcurrentHashMap<>();

    public OpenWeatherService(TrekRepository treks,
                              RestClient.Builder builder,
                              @Value("${app.openweather.base-url}") String url,
                              @Value("${app.openweather.api-key}") String key) {
        this.treks = treks;
        this.key = key;

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(REQUEST_TIMEOUT_MILLIS);
        requestFactory.setReadTimeout(REQUEST_TIMEOUT_MILLIS);
        this.client = builder.baseUrl(url).requestFactory(requestFactory).build();
    }

    @Override
    public WeatherResponse getCurrentWeather(Long id) {
        Cached cached = cache.get(id);
        if (cached != null && cached.expiresAt().isAfter(Instant.now())) {
            return cached.value();
        }
        if (key.isBlank()) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Weather data is not configured.");
        }

        Trek trek = treks.findById(id).orElseThrow(() -> new NotFoundException("Trek not found."));
        JsonNode body = client.get()
                .uri(uriBuilder -> uriBuilder.path("/weather")
                        .queryParam("lat", trek.getLatitude())
                        .queryParam("lon", trek.getLongitude())
                        .queryParam("appid", key)
                        .queryParam("units", "metric")
                        .build())
                .retrieve()
                .body(JsonNode.class);

        WeatherResponse response = new WeatherResponse(
                body.path("main").path("temp").decimalValue(),
                body.path("main").path("humidity").asInt(),
                body.path("wind").path("speed").decimalValue(),
                body.path("rain").has("1h")
                        ? body.path("rain").path("1h").decimalValue().multiply(BigDecimal.valueOf(100))
                        : BigDecimal.ZERO,
                body.path("weather").path(0).path("icon").asText());
        cache.put(id, new Cached(response, Instant.now().plus(CACHE_DURATION)));
        return response;
    }

    private record Cached(WeatherResponse value, Instant expiresAt) {
    }
}
