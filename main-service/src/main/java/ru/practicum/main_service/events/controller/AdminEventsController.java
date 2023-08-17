package ru.practicum.main_service.events.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main_service.events.dto.EventFullDto;
import ru.practicum.main_service.events.service.EventsService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class AdminEventsController {
    private final EventsService eventsService;
//    @GetMapping("/{id}")
//    public EventFullDto getEventById(@PathVariable(name = "id") Long eventId, String ip) {
//
//        return eventsService.getEventById(eventId, ip);
//    }
}
