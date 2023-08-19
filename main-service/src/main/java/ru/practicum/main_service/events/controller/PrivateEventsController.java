package ru.practicum.main_service.events.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.events.dto.EventsFullDto;
import ru.practicum.main_service.events.dto.NewEventDto;
import ru.practicum.main_service.events.service.EventsService;

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

}
