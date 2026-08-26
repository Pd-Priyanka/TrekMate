package com.trekmate;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trekmate.entity.Role;
import com.trekmate.entity.User;
import com.trekmate.repository.UserRepository;

import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class FavoriteIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @LocalServerPort
    private int port;

    @Test
    void favoriteLifecycleReturnsAccurateCountsAndPreventsDuplicates() throws Exception {
        HttpHeaders headers = bearerHeaders(registerUser().token());
        Long trekId = firstTrekId(headers);

        ResponseEntity<String> created = restTemplate.exchange(url("/api/favorites/" + trekId), HttpMethod.POST, new HttpEntity<>(headers), String.class);
        assertThat(created.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        JsonNode createdBody = objectMapper.readTree(created.getBody());
        assertThat(createdBody.path("favoriteCount").asLong()).isEqualTo(1);
        assertThat(createdBody.path("favorite").path("trek").path("id").asLong()).isEqualTo(trekId);

        ResponseEntity<String> duplicate = restTemplate.exchange(url("/api/favorites/" + trekId), HttpMethod.POST, new HttpEntity<>(headers), String.class);
        assertThat(duplicate.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);

        ResponseEntity<String> listed = restTemplate.exchange(url("/api/favorites"), HttpMethod.GET, new HttpEntity<>(headers), String.class);
        assertThat(listed.getStatusCode()).isEqualTo(HttpStatus.OK);
        JsonNode listedBody = objectMapper.readTree(listed.getBody());
        assertThat(listedBody.path("favoriteCount").asLong()).isEqualTo(1);
        assertThat(listedBody.path("favorites")).hasSize(1);

        ResponseEntity<String> removed = restTemplate.exchange(url("/api/favorites/" + trekId), HttpMethod.DELETE, new HttpEntity<>(headers), String.class);
        assertThat(removed.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(objectMapper.readTree(removed.getBody()).path("favoriteCount").asLong()).isZero();
    }

    @Test
    void favoritesRequireAuthentication() {
        ResponseEntity<String> response = restTemplate.getForEntity(url("/api/favorites"), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void trekMutationsRequireAdminRole() throws Exception {
        HttpHeaders headers = bearerHeaders(registerUser().token());
        ResponseEntity<String> response = restTemplate.exchange(url("/api/treks"), HttpMethod.POST, new HttpEntity<>(headers), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void databaseManagedAdminCanCreateTrek() throws Exception {
        RegisteredUser registeredUser = registerUser();
        User user = userRepository.findByEmailIgnoreCase(registeredUser.email()).orElseThrow();
        user.setRole(Role.ADMIN);
        userRepository.saveAndFlush(user);

        String slug = "admin-trek-" + UUID.randomUUID();
        HttpHeaders headers = bearerHeaders(registeredUser.token());
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> request = Map.ofEntries(
                Map.entry("name", "Admin Test Trek"), Map.entry("slug", slug), Map.entry("location", "Test Location"),
                Map.entry("state", "Test State"), Map.entry("country", "India"), Map.entry("difficulty", "EASY"),
                Map.entry("distanceKm", 12.5), Map.entry("durationDays", 2), Map.entry("altitudeMeters", 1500),
                Map.entry("bestSeason", "October"), Map.entry("description", "Created by an admin integration test."),
                Map.entry("latitude", 20.0), Map.entry("longitude", 75.0));
        ResponseEntity<String> response = restTemplate.postForEntity(url("/api/treks"), new HttpEntity<>(request, headers), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void reviewsValidateDuplicatesAndOwnerPermissions() throws Exception {
        HttpHeaders ownerHeaders = bearerHeaders(registerUser().token());
        ownerHeaders.setContentType(MediaType.APPLICATION_JSON);
        Long trekId = firstTrekId(ownerHeaders);
        Map<String, Object> review = Map.of("rating", 5, "comment", "A well-organized and memorable trek.");

        ResponseEntity<String> created = restTemplate.postForEntity(url("/api/treks/" + trekId + "/reviews"), new HttpEntity<>(review, ownerHeaders), String.class);
        assertThat(created.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Long reviewId = objectMapper.readTree(created.getBody()).path("id").asLong();

        ResponseEntity<String> duplicate = restTemplate.postForEntity(url("/api/treks/" + trekId + "/reviews"), new HttpEntity<>(review, ownerHeaders), String.class);
        assertThat(duplicate.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);

        ResponseEntity<String> invalidRating = restTemplate.postForEntity(url("/api/treks/" + trekId + "/reviews"), new HttpEntity<>(Map.of("rating", 6, "comment", "Invalid rating"), ownerHeaders), String.class);
        assertThat(invalidRating.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        HttpHeaders otherHeaders = bearerHeaders(registerUser().token());
        otherHeaders.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<String> forbidden = restTemplate.exchange(url("/api/reviews/" + reviewId), HttpMethod.PUT, new HttpEntity<>(review, otherHeaders), String.class);
        assertThat(forbidden.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

        ResponseEntity<String> deleted = restTemplate.exchange(url("/api/reviews/" + reviewId), HttpMethod.DELETE, new HttpEntity<>(ownerHeaders), String.class);
        assertThat(deleted.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    private RegisteredUser registerUser() throws Exception {
        String email = "favorite-" + UUID.randomUUID() + "@example.com";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<String> response = restTemplate.postForEntity(url("/api/auth/register"), new HttpEntity<>(Map.of("name", "Favorite Tester", "email", email, "password", "Password123"), headers), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        return new RegisteredUser(objectMapper.readTree(response.getBody()).path("accessToken").asText(), email);
    }

    private Long firstTrekId(HttpHeaders headers) throws Exception {
        ResponseEntity<String> response = restTemplate.exchange(url("/api/treks?size=1"), HttpMethod.GET, new HttpEntity<>(headers), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        JsonNode content = objectMapper.readTree(response.getBody()).path("content");
        return content.get(0).path("id").asLong();
    }

    private HttpHeaders bearerHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return headers;
    }

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    private record RegisteredUser(String token, String email) {
    }
}
