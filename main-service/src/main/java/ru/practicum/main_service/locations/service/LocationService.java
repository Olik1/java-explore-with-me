package ru.practicum.main_service.locations.service;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.main_service.locations.dto.NewLocationtDto;

public interface LocationService {
    NewLocationtDto createLocation(NewLocationtDto newLocationtDto);

    void deleteLocation(long id);

    NewLocationtDto updateLocation(long id, NewLocationtDto newLocationtDto);

}
