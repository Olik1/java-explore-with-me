package ru.practicum.main_service.locations.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.main_service.locations.model.Location;

@UtilityClass
public class LocationMapper {
    public static final Location toLocation(LocationDto locationDto) {
        return Location.builder()
                .lat(locationDto.getLat())
                .lon(locationDto.getLon())
                .build();
    }

    public static final LocationDto toLocationDto(Location location) {
        return LocationDto.builder()
                .lat(location.getLat())
                .lon(location.getLon())
                .build();
    }
    public static Location toLocation(NewLocationtDto newLocationtDto) {
        return Location.builder()
                .name(newLocationtDto.getName())
                .status(newLocationtDto.getStatus())
                .lat(newLocationtDto.getLat())
                .lon(newLocationtDto.getLon())
                .radius(newLocationtDto.getRadius())
                .build();
    }
    public static NewLocationtDto toNewLocationtDto(Location newLocationtDto) {
        return NewLocationtDto.builder()
                .id(newLocationtDto.getId())
                .name(newLocationtDto.getName())
                .lat(newLocationtDto.getLat())
                .lon(newLocationtDto.getLon())
                .radius(newLocationtDto.getRadius())
                .build();
    }
}
