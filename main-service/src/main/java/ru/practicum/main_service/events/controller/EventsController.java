package ru.practicum.main_service.events.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.events.dto.EventFullDto;
import ru.practicum.main_service.events.dto.EventShortDto;
import ru.practicum.main_service.events.model.SortEvents;
import ru.practicum.main_service.events.service.EventsService;

import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventsController {
    private final EventsService eventsService;

    @GetMapping
    public List<EventShortDto> getEvents(@RequestParam(required = false) String text,
                                             @RequestParam(required = false) List<Long> categories,
                                             @RequestParam(required = false) Boolean paid,
                                             @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                             LocalDateTime rangeStart,
                                             @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                             LocalDateTime rangeEnd,
                                             @RequestParam(required = false) Boolean onlyAvailable,
                                             @RequestParam(required = false) SortEvents sort,
                                             @RequestParam(required = false, defaultValue = "0")
                                             @PositiveOrZero Integer from,
                                             @RequestParam(required = false, defaultValue = "10")
                                             @PositiveOrZero Integer size)

    {
        return eventsService.getEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }
    @GetMapping("/{id}")
    public EventFullDto getEventById(@PathVariable(name = "id") Long eventId) {

        return eventsService.getEventById(eventId);
    }

}
