package ru.practicum.main_service.locations.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.main_service.exception.ConflictException;
import ru.practicum.main_service.exception.ObjectNotFoundException;
import ru.practicum.main_service.locations.LocationRepository;
import ru.practicum.main_service.locations.dto.LocationMapper;
import ru.practicum.main_service.locations.dto.LocationResponseDto;
import ru.practicum.main_service.locations.dto.NewLocationDto;
import ru.practicum.main_service.locations.dto.UpdateLocationDto;
import ru.practicum.main_service.locations.model.Location;
import ru.practicum.main_service.locations.model.LocationStatus;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;

    @Override
    public LocationResponseDto createLocation(NewLocationDto newLocationDto, boolean isAdmin) {
        if (locationRepository.existsLocationByName(newLocationDto.getName())) {
            throw new ConflictException("Такая локация уже существует!");
        }
        if (locationRepository.existsLocationByLatAndLon(newLocationDto.getLat(), newLocationDto.getLon())) {
            throw new ConflictException("Такие координаты уже существуют!");
        }
        Location location = LocationMapper.toLocation(newLocationDto);
        location.setStatus(isAdmin ? LocationStatus.APPROVED : LocationStatus.PENDING);
        locationRepository.save(location);
        log.info("Запрос POST на добавление локации, с id: {}", location.getId());
        var result = LocationMapper.toNewLocationtDto(location);
        return result;
    }

    @Override
    public void deleteLocation(long id) {
        var location = findLocationById(id);
        if (location.getName() == null || location.getRadius() == null) {
            throw new ObjectNotFoundException("Эта локация не найдена!");
        }
        locationRepository.deleteById(id);
        log.info("Запрос DELETE на удаление локации, с id: {}", id);
    }

    @Override
    public LocationResponseDto updateLocation(long id, UpdateLocationDto updateLocationDto) {
        Location location = findLocationById(id);
        if (updateLocationDto.getLat() != null) {
            location.setLat(updateLocationDto.getLat());
        }
        if (updateLocationDto.getLon() != null) {
            location.setLon(updateLocationDto.getLon());
        }
        if (updateLocationDto.getName() != null) {
            location.setName(updateLocationDto.getName());
        }
        if (updateLocationDto.getRadius() != null) {
            location.setRadius(updateLocationDto.getRadius());
        }

        locationRepository.save(location);
        return LocationMapper.toNewLocationtDto(location);
    }

    @Override
    public LocationResponseDto getLocation(long id) {
        Location location = findLocationById(id);
        if (location.getStatus() != LocationStatus.APPROVED) {
            throw new ConflictException("Данная локация недоступна!");
        }
        log.info("Запрос GET на получение локации");
        var result = LocationMapper.toLocationResponseDto(location);
        return result;
    }

    @Override
    public List<LocationResponseDto> getLocations(Integer from, Integer size) {
        int offset = from > 0 ? from / size : 0;
        PageRequest page = PageRequest.of(offset, size, Sort.by("id"));
        List<Location> locationList = locationRepository.findAll(page).getContent();
        log.info("Запрос GET на получение списка категорий");
        var result = locationList.stream().map(LocationMapper::toLocationResponseDto).collect(Collectors.toList());
        return result;
    }

    @Override
    public LocationResponseDto confirmLocation(long id, boolean approved) {
        Location location = findLocationById(id);
        location.setStatus(approved ? LocationStatus.APPROVED : LocationStatus.CANCELED);
        locationRepository.save(location);
        return LocationMapper.toNewLocationtDto(location);
    }


    private Location findLocationById(long id) {
        return locationRepository.findById(id).orElseThrow(
                () -> new ObjectNotFoundException("Такой локации не существует!"));
    }

}
