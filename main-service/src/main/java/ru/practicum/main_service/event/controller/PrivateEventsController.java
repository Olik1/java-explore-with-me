package ru.practicum.main_service.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.event.dto.*;
import ru.practicum.main_service.event.service.EventsService;
import ru.practicum.main_service.request.dto.ParticipationRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
public class PrivateEventsController {
    private final EventsService eventsService;

    @GetMapping
    public List<EventsFullDto> getAllEventsByUserId(@PathVariable Long userId,
                                                    @RequestParam(required = false, defaultValue = "0")
                                                    @PositiveOrZero Integer from,
                                                    @RequestParam(required = false, defaultValue = "10")
                                                    @PositiveOrZero Integer size) {

        return eventsService.getAllEventsByUserId(userId, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventsFullDto createEvents(@PathVariable Long userId,
                                      @RequestBody @Valid NewEventDto newEventDto) {
        return eventsService.createEvents(userId, newEventDto);
    }

    @GetMapping("/{eventId}")
    public EventsFullDto getEventsByUserId(@PathVariable Long userId,
                                           @PathVariable Long eventId) {
        return eventsService.getEventsByUserId(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventsFullDto updateEventsByUser(@PathVariable Long userId,
                                            @PathVariable Long eventId,
                                            @RequestBody @Valid UpdateEventUserRequestDto requestDto) {
        return eventsService.updateEventsByUser(userId, eventId, requestDto);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestUserEvents(@PathVariable Long userId,
                                                              @PathVariable Long eventId) {
        return eventsService.getRequestUserEvents(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateStatusRequestByUserIdForEvents(@PathVariable Long userId,
                                                                               @PathVariable Long eventId,
                                                                               @RequestBody @Valid EventRequestStatusUpdateRequest requestDto) {
        return eventsService.updateStatusRequestByUserIdForEvents(userId, eventId, requestDto);
    }
}
