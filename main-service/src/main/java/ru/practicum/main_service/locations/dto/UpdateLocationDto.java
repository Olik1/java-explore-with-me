package ru.practicum.main_service.locations.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.main_service.locations.model.LocationStatus;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

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