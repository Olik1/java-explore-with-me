package ru.practicum.main_service.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.main_service.categories.dto.CategoryDto;
import ru.practicum.main_service.users.dto.UserDto;

import java.time.LocalDateTime;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventShortDto {

    private Long id;

    private String annotation;

    private CategoryDto category;

    /**
     * Количество одобренных заявок на участие в данном событии
     */
    private Long confirmedRequests;

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
