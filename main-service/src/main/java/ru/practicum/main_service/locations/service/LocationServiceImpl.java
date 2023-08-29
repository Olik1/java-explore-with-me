package ru.practicum.main_service.locations.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.main_service.exception.ObjectNotFoundException;
import ru.practicum.main_service.locations.LocationRepository;
import ru.practicum.main_service.locations.dto.LocationMapper;
import ru.practicum.main_service.locations.dto.NewLocationtDto;
import ru.practicum.main_service.locations.model.Location;
import ru.practicum.main_service.locations.model.LocationStatus;

import javax.validation.ValidationException;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;

    @Override
    public NewLocationtDto createLocation(NewLocationtDto newLocationtDto) {
        if (locationRepository.existsLocationByName(newLocationtDto.getName())) {
            throw new ValidationException("Такая локация уже существует!");
        }
        if (locationRepository.existsLocationByLatAndLon(newLocationtDto.getLat(), newLocationtDto.getLon())) {
            throw new ValidationException("Такие координаты уже существуют!");
        }
        Location location = LocationMapper.toLocation(newLocationtDto);
        locationRepository.save(location);
        log.info("Запрос POST на добавление локации, с id: {}", newLocationtDto.getId());
        var result = LocationMapper.toNewLocationtDto(location);
        result.setStatus(LocationStatus.APPROVED);
        return result;
    }

    @Override
    public void deleteLocation(long id) {
        findLocationById(id);
        locationRepository.deleteById(id);
        log.info("Запрос DELETE на удаление локации, с id: {}", id);
    }

    @Override
    public NewLocationtDto updateLocation(long id, NewLocationtDto newLocationtDto) {
        Location location = findLocationById(id);
        if(newLocationtDto.getLat() != null) {
            location.setLat(newLocationtDto.getLat());
        }
        if(newLocationtDto.getLon() != null) {
            location.setLon(newLocationtDto.getLon());
        }
        if(newLocationtDto.getName() != null) {
            location.setName(newLocationtDto.getName());
        }
        if(newLocationtDto.getRadius() != null) {
            location.setRadius(newLocationtDto.getRadius());
        }
        if(newLocationtDto.getStatus() != null) {
            location.setStatus(newLocationtDto.getStatus());
        }
        locationRepository.save(location);
        return LocationMapper.toNewLocationtDto(location);
    }

    private Location findLocationById(long id) {
        return locationRepository.findById(id).orElseThrow(
                () -> new ObjectNotFoundException("Такой локации не существует!"));
    }

}
