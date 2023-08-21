package ru.practicum.main_service.event.service;

import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.main_service.event.dto.*;
import ru.practicum.main_service.event.model.SortEvents;
import ru.practicum.main_service.event.model.State;
import ru.practicum.main_service.request.dto.ParticipationRequestDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public interface EventService {
    List<EventShortDto> getEvents(String text, List<Long> categories, Boolean paid,
                                  LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                  Boolean onlyAvailable, SortEvents sort, Integer from, Integer size);

    EventFullDto getEventById(Long eventId, String ip);

    List<EventFullDto> getAllEventsByUserId(Long userId, Integer from, Integer size);

    EventFullDto createEvents(Long userId, NewEventDto newEventDto);

    EventFullDto getEventsByUserId(Long userId, Long eventId);

    EventFullDto updateEventsByUser(Long userId, Long eventId, UpdateEventUserRequestDto requestDto);

    List<ParticipationRequestDto> getRequestUserEvents(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateStatusRequestByUserIdForEvents
            (Long userId, Long eventId,
             EventRequestStatusUpdateRequest requestDto);

    List<EventFullDto> adminGetEvents(List<Long> userIds, List<State> states, List<Long> categories,
                                      String rangeStart, String rangeEnd, Integer from, Integer size);

}
