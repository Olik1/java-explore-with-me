package ru.practicum.main_service.locations.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.locations.dto.LocationResponseDto;
import ru.practicum.main_service.locations.dto.NewLocationDto;
import ru.practicum.main_service.locations.dto.UpdateLocationDto;
import ru.practicum.main_service.locations.service.LocationService;

import javax.validation.Valid;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/locations")
public class AdminLocationController {
    private final LocationService locationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LocationResponseDto createLocation(@RequestBody @Valid NewLocationDto newLocationDto) {
        var result = locationService.createLocation(newLocationDto, true);
        return result;
    }

    @PatchMapping("/{id}")
    public LocationResponseDto updateLocation(@PathVariable long id,
                                              @RequestBody @Valid UpdateLocationDto updateLocationDto) {
        return locationService.updateLocation(id, updateLocationDto);
    }

    @DeleteMapping("/{id}")
    public void deleteLocation(@PathVariable long id) {
        locationService.deleteLocation(id);
    }

    @PatchMapping("/confirm/{id}")
    public LocationResponseDto confirmLocation(@PathVariable long id,
                                               @RequestBody boolean approved) {
        return locationService.confirmLocation(id, approved);
    }
}
