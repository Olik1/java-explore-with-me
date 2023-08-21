package ru.practicum.main_service.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.main_service.categories.dto.CategoryDto;
import ru.practicum.main_service.event.model.Location;
import ru.practicum.main_service.event.model.State;
import ru.practicum.main_service.users.dto.UserDto;

import javax.persistence.Transient;
import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventFullDto {
    private String annotation;
    private CategoryDto categoryDto;
    private Integer confirmedRequests;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdOn;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private Long id;
    private UserDto initiator;
    private LocationDto location;
    private Boolean paid;
    private Integer participantLimit;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    private State state;
    private String title;
    private Long views;


}
