package ru.practicum.main_service.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.event.dto.EventFullDto;
import ru.practicum.main_service.event.dto.UpdateEventRequestDto;
import ru.practicum.main_service.event.model.State;
import ru.practicum.main_service.event.service.EventService;

import javax.validation.constraints.Min;
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

    @PatchMapping("/{eventId}")
    public EventFullDto patchAdminEvent(@PathVariable @Min(1) Long eventId,
                                        @RequestBody @Validated UpdateEventRequestDto requestDto) {
        return eventService.adminUpdateEvent(eventId, requestDto);
    }
}
