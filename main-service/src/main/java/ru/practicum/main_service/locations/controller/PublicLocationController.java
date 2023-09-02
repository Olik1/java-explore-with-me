package ru.practicum.main_service.locations.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.locations.dto.LocationResponseDto;
import ru.practicum.main_service.locations.dto.NewLocationDto;
import ru.practicum.main_service.locations.model.LocationStatus;
import ru.practicum.main_service.locations.service.LocationService;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/locations")
public class PublicLocationController {
    private final LocationService locationService;

    @GetMapping("/{id}")
    public LocationResponseDto getLocation(@PathVariable long id) {
        return locationService.getLocation(id);
    }

    @GetMapping
    public List<LocationResponseDto> getLocations(@RequestParam(defaultValue = "0") Integer from,
                                                  @RequestParam(defaultValue = "10") Integer size) {
        return locationService.getLocations(from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LocationResponseDto createLocationByUser(@RequestBody @Valid NewLocationDto newLocationDto) {
        var result = locationService.createLocation(newLocationDto, false);
        return result;
    }
}
