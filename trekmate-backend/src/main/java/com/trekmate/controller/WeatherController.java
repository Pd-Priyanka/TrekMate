package com.trekmate.controller;
import com.trekmate.dto.WeatherResponse; import com.trekmate.service.WeatherService; import io.swagger.v3.oas.annotations.security.SecurityRequirement; import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/weather") @SecurityRequirement(name="bearerAuth") public class WeatherController {private final WeatherService service;public WeatherController(WeatherService service){this.service=service;}@GetMapping("/{trekId}") public WeatherResponse get(@PathVariable Long trekId){return service.getCurrentWeather(trekId);}}
