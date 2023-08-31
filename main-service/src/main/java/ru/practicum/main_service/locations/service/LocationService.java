package ru.practicum.main_service.locations.service;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.main_service.locations.dto.LocationResponseDto;
import ru.practicum.main_service.locations.dto.NewLocationtDto;

import java.util.List;

public interface LocationService {
    NewLocationtDto createLocation(NewLocationtDto newLocationtDto);

    void deleteLocation(long id);

    NewLocationtDto updateLocation(long id, NewLocationtDto newLocationtDto);
    LocationResponseDto getLocation(long id);
    List<LocationResponseDto> getLocations(Integer from, Integer size);

}
