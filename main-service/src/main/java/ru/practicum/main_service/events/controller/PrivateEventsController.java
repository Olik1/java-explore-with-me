package ru.practicum.main_service.events.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.events.dto.EventsFullDto;
import ru.practicum.main_service.events.service.EventsService;

import javax.validation.constraints.PositiveOrZero;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
public class PrivateEventsController {
    private final EventsService eventsService;
    @GetMapping
    public EventsFullDto getEventsByUserId(@PathVariable Long userId,
                                           @RequestParam(required = false, defaultValue = "0")
                                           @PositiveOrZero Integer from,
                                           @RequestParam(required = false, defaultValue = "10")
                                           @PositiveOrZero Integer size) {

        return eventsService.getEventsByUserId(userId, from, size);
    }
}
