package ru.practicum.main_service.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Value;
import ru.practicum.main_service.categories.dto.CategoryDto;
import ru.practicum.main_service.users.dto.UserDto;

import java.time.LocalDateTime;
@Data
@Value
@Builder
@AllArgsConstructor
public class EventsShortDto {

    private Long id;

    private String annotation;

    private CategoryDto category;

    /**
     * Количество одобренных заявок на участие в данном событии
     */
    private Integer confirmedRequests;

    /**
     * Дата и время на которые намечено событие
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    /**
     * Инициатор события
     */
    private UserDto initiator;

    /**
     * Нужно ли оплачивать участие
     */
    private Boolean paid;

    private String title;

    /**
     * Количество просмотрев события
     */
    private Integer views;
}
