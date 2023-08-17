package ru.practicum.main_service.events.service;

import ru.practicum.main_service.events.dto.EventFullDto;
import ru.practicum.main_service.events.dto.EventShortDto;
import ru.practicum.main_service.events.model.SortEvents;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventsService {
    List<EventShortDto> getEvents(String text, List<Long> categories, Boolean paid,
                                  LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                  Boolean onlyAvailable, SortEvents sort, Integer from, Integer size);

    EventFullDto getEventById(Long eventId, String ip);
}
