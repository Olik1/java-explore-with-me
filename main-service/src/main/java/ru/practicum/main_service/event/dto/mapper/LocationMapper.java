package ru.practicum.main_service.event.dto.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.main_service.event.dto.LocationDto;
import ru.practicum.main_service.event.model.Location;

@UtilityClass
public class LocationMapper {
    public static final Location toLocation(LocationDto locationDto) {
        return Location.builder()
                .lat(locationDto.getLat())
                .lon(locationDto.getLon())
                .build();
    }
}
