package ru.practicum.main_service.events.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.main_service.events.model.Location;

@UtilityClass
public class LocationMapper {
    public static final Location toDto(LocationDto locationDto) {
        return Location.builder()
                .lat(locationDto.getLat())
                .lon(locationDto.getLon())
                .build();
    }
}
