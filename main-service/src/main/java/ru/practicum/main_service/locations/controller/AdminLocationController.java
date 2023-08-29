package ru.practicum.main_service.locations.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.locations.dto.NewLocationtDto;
import ru.practicum.main_service.locations.service.LocationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/locations")
public class AdminLocationController {
    private final LocationService locationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NewLocationtDto createLocation(@RequestBody NewLocationtDto newLocationtDto) {
        var result = locationService.createLocation(newLocationtDto);
        return result;
    }
    @PatchMapping("/{id}")
    public NewLocationtDto updateLocation(@PathVariable long id, @RequestBody NewLocationtDto newLocationtDto) {
        return locationService.updateLocation(id, newLocationtDto);
    }
    @DeleteMapping("/{id}")
    public void deleteLocation(@PathVariable long id) {
        locationService.deleteLocation(id);
    }

}
