package ru.practicum.main_service.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.event.dto.*;
import ru.practicum.main_service.event.service.EventService;
import ru.practicum.main_service.request.dto.ParticipationRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
public class PrivateEventController {
    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> getAllEventsByUserId(@PathVariable Long userId,
                                                   @RequestParam(required = false, defaultValue = "0")
                                                   @PositiveOrZero Integer from,
                                                   @RequestParam(required = false, defaultValue = "10")
                                                   @PositiveOrZero Integer size) {

        return eventService.getAllEventsByUserId(userId, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvents(@PathVariable Long userId,
                                     @RequestBody @Valid NewEventDto newEventDto) {
        return eventService.createEvents(userId, newEventDto);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventsByUserId(@PathVariable Long userId,
                                          @PathVariable Long eventId) {
        return eventService.getEventsByUserId(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventsByUser(@PathVariable Long userId,
                                           @PathVariable Long eventId,
                                           @RequestBody @Valid UpdateEventRequestDto requestDto) {
        return eventService.updateEventsByUser(userId, eventId, requestDto);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestUserEvents(@PathVariable Long userId,
                                                              @PathVariable Long eventId) {
        return eventService.getRequestUserEvents(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateStatusRequestByUserIdForEvents(@PathVariable Long userId,
                                                                               @PathVariable Long eventId,
                                                                               @RequestBody @Valid EventRequestStatusUpdateRequest requestDto) {
        return eventService.updateStatusRequestByUserIdForEvents(userId, eventId, requestDto);
    }
}
