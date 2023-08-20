package ru.practicum.main_service.event.service;

import ru.practicum.main_service.event.dto.*;
import ru.practicum.main_service.event.model.SortEvents;
import ru.practicum.main_service.request.dto.ParticipationRequestDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EventsService {
    List<EventsShortDto> getEvents(String text, List<Long> categories, Boolean paid,
                                   LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                   Boolean onlyAvailable, SortEvents sort, Integer from, Integer size);

    EventsFullDto getEventById(Long eventId, String ip);

    List<EventsFullDto> getAllEventsByUserId(Long userId, Integer from, Integer size);

    EventsFullDto createEvents(Long userId, NewEventDto newEventDto);

    EventsFullDto getEventsByUserId(Long userId, Long eventId);

    EventsFullDto updateEventsByUser(Long userId, Long eventId, UpdateEventUserRequestDto requestDto);

    List<ParticipationRequestDto> getRequestUserEvents(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateStatusRequestByUserIdForEvents(Long userId, Long eventId,
                                                                        EventRequestStatusUpdateRequest requestDto);
}
