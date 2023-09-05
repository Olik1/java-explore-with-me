package ru.practicum.main_service.locations.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;

@Data
@Builder
@AllArgsConstructor
public class NewLocationDto {
    @NotNull
    @Min(-90)
    @Max(90)
    private Float lat;
    @NotNull
    @Min(-180)
    @Max(180)
    private Float lon;
    @NotBlank
    @Size(max = 120)
    private String name;
    @NotNull
    @Positive
    private Float radius;
}
