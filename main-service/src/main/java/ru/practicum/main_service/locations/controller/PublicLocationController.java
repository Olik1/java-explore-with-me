package ru.practicum.main_service.locations.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.locations.dto.LocationResponseDto;
import ru.practicum.main_service.locations.service.LocationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/locations")
public class PublicLocationController {
    private final LocationService locationService;

    /*
GET /locations/{locationId} - Получение локации по Id
GET /locations – получение списка всех открытых локаций
GET /events/{locationId}/locations – получение списка событий по локации
Возможность поиска событий в конкретной локации
     */
    @GetMapping("/{id}")
    public LocationResponseDto getLocation(@PathVariable long id) {
        return locationService.getLocation(id);
    }

    @GetMapping
    public List<LocationResponseDto> getLocations(@RequestParam(defaultValue = "0") Integer from,
                                                  @RequestParam(defaultValue = "10") Integer size) {
        return locationService.getLocations(from, size);
    }

}
