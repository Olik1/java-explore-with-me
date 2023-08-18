package ru.practicum.main_service.events.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.main_service.categories.dto.CategoriesMapper;
import ru.practicum.main_service.categories.dto.CategoryDto;
import ru.practicum.main_service.events.model.Events;
import ru.practicum.main_service.users.dto.UserDto;
import ru.practicum.main_service.users.dto.UserMapper;

import java.time.format.DateTimeFormatter;

@UtilityClass
public class EventsMapper {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static EventsFullDto eventFullDto(Events events) {
        return EventsFullDto.builder()
                .annotation(events.getAnnotation())
                .categoryDto(CategoriesMapper.toCategoryDto(events.getCategories()))
//                .confirmedRequests(0)
                .createdOn(events.getCreatedOn())
                .description(events.getDescription())
                .eventDate(events.getEventDate())
                .id(events.getId())
                .initiator(UserMapper.toUserDto(events.getInitiator()))
                .paid(events.getPaid())
                .participantLimit(events.getParticipantLimit())
                .publishedOn(events.getPublishedOn())
                .requestModeration(events.getRequestModeration())
                .state(events.getState())
                .title(events.getTitle())
                .build();
    }
    public static EventsShortDto toEventShortDto(Events events, CategoryDto categoryDto, UserDto userDto) {
        return EventsShortDto.builder()
                .id(events.getId())
                .annotation(events.getAnnotation())
                .category(categoryDto)
                .confirmedRequests(events.getParticipantLimit())
                .eventDate(events.getEventDate())
                .initiator(userDto)
                .paid(events.getPaid())
                .title(events.getTitle())
                .build();

    }
}
