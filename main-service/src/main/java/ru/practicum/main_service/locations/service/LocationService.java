package ru.practicum.main_service.locations.service;

import ru.practicum.main_service.locations.dto.LocationResponseDto;
import ru.practicum.main_service.locations.dto.NewLocationDto;
import ru.practicum.main_service.locations.dto.UpdateLocationDto;

import java.util.List;

public interface LocationService {
    LocationResponseDto createLocation(NewLocationDto newLocationDto, boolean isAdmin);

    void deleteLocation(long id);

    LocationResponseDto updateLocation(long id, UpdateLocationDto updateLocationDto);

    LocationResponseDto getLocation(long id);

    List<LocationResponseDto> getLocations(Integer from, Integer size);

    LocationResponseDto confirmLocation(long id, boolean approved);

}
