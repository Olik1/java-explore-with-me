package ru.practicum.main_service.events.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.main_service.categories.dto.CategoriesMapper;
import ru.practicum.main_service.events.model.Events;
import ru.practicum.main_service.users.dto.UserMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class EventsMapper {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static EventFullDto eventFullDto(Events events) {
        return EventFullDto.builder()
                .annotation(events.getAnnotation())
                .categoryDto(CategoriesMapper.toCategoryDto(events.getCategories()))
//                .confirmedRequests(0)
                .createdOn(events.getCreatedOn())
                .description(events.getDescription())
                .eventDate(events.getEventDate())
                .id(events.getId())
                .initiator(UserMapper.toUserDto(events.getCreator()))
                .paid(events.getPaid())
                .participantLimit(events.getParticipantLimit())
                .publishedOn(events.getPublishedOn())
                .requestModeration(events.getRequestModeration())
                .state(events.getState())
                .title(events.getTitle())
                .build();
    }
}
/*


    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    private String state;
    private String title;
    private Long views;
 */
