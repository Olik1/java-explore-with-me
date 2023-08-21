package ru.practicum.main_service.event.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.main_service.event.model.State;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class Criteria {
    private List<Long> users;

    private List<State> states;

    private List<Long> categories;

    private LocalDateTime rangeStart;

    private LocalDateTime rangeEnd;

    private Integer from;

    private Integer size;
}
