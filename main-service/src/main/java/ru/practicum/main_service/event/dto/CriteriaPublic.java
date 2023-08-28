package ru.practicum.main_service.event.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.main_service.event.model.SortEvents;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CriteriaPublic {
    private String text;
    private List<Long> categories;
    private Boolean paid;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private Boolean onlyAvailable;
    private SortEvents sort;
    private Integer from;
    private Integer size;
}
