package ru.practicum.main_service.events.service;

import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.main_service.events.dto.EventsFullDto;
import ru.practicum.main_service.events.dto.EventsShortDto;
import ru.practicum.main_service.events.dto.NewEventDto;
import ru.practicum.main_service.events.model.SortEvents;

import java.time.LocalDateTime;
import java.util.List;

public interface EventsService {
    List<EventsShortDto> getEvents(String text, List<Long> categories, Boolean paid,
                                   LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                   Boolean onlyAvailable, SortEvents sort, Integer from, Integer size);

    EventsFullDto getEventById(Long eventId, String ip);

    List<EventsFullDto> getAllEventsByUserId(Long userId, Integer from, Integer size);
    EventsFullDto createEvents(Long userId, NewEventDto newEventDto);
    EventsFullDto getEventsByUserId(Long userId,Long eventId);
}
