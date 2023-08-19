package ru.practicum.main_service.events.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
public class LocationDto {
    @NotNull
    private float lat;
    @NotNull
    private float lon;
}