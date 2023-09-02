package ru.practicum.main_service.locations.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.main_service.locations.model.LocationStatus;

import javax.validation.constraints.*;

@Data
@Builder
@AllArgsConstructor
public class UpdateLocationDto {
    @Min(-90)
    @Max(90)
    private Float lat;
    @Min(-180)
    @Max(180)
    private Float lon;
    @Size(max = 120)
    private String name;
    @Positive
    private Float radius;
    private LocationStatus status;
}