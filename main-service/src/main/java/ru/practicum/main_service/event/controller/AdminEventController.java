package ru.practicum.main_service.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main_service.event.dto.Criteria;
import ru.practicum.main_service.event.dto.EventFullDto;
import ru.practicum.main_service.event.model.State;
import ru.practicum.main_service.event.service.EventService;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class AdminEventController {
    private final EventService eventService;

    @GetMapping()
    public List<EventFullDto> adminGetEvents(@RequestParam(required = false) List<Long> users,
                                             @RequestParam(required = false) List<State> states,
                                             @RequestParam(required = false) List<Long> categories,
                                             @RequestParam(required = false) String rangeStart,
                                             @RequestParam(required = false) String rangeEnd,
                                             @RequestParam(defaultValue = "0") Integer from,
                                             @RequestParam(defaultValue = "10") Integer size) {

        return eventService.adminGetEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }
}
