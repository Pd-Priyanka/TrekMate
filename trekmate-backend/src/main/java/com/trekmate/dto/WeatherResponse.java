package com.trekmate.dto;
import java.math.BigDecimal;
public record WeatherResponse(BigDecimal temperatureCelsius, Integer humidity, BigDecimal windSpeed, BigDecimal rainProbability, String weatherIcon) { }
