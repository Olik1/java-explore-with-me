package ru.practicum.main_service.locations.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class LocationResponseDto {
    private long id;
    private Float lat;
    private Float lon;
    private String name;
    private Float radius;
}
