package ru.practicum.main_service.event.dto.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.main_service.categories.dto.CategoriesMapper;
import ru.practicum.main_service.categories.dto.CategoryDto;
import ru.practicum.main_service.categories.model.Categories;
import ru.practicum.main_service.event.dto.EventsFullDto;
import ru.practicum.main_service.event.dto.EventsShortDto;
import ru.practicum.main_service.event.dto.NewEventDto;
import ru.practicum.main_service.event.model.Event;
import ru.practicum.main_service.event.model.Location;
import ru.practicum.main_service.event.model.State;
import ru.practicum.main_service.users.dto.UserDto;
import ru.practicum.main_service.users.dto.UserMapper;
import ru.practicum.main_service.users.model.User;

import java.time.format.DateTimeFormatter;

@UtilityClass
public class EventsMapper {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static EventsFullDto toEventFullDto(Event event) {
        return EventsFullDto.builder()
                .annotation(event.getAnnotation())
                .categoryDto(CategoriesMapper.toCategoryDto(event.getCategories()))
//                .confirmedRequests(0)
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(UserMapper.toUserDto(event.getInitiator()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .build();
    }
    public static EventsShortDto toEventShortDto(Event event, CategoryDto categoryDto, UserDto userDto) {
        return EventsShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(categoryDto)
                .confirmedRequests(event.getParticipantLimit())
                .eventDate(event.getEventDate())
                .initiator(userDto)
                .paid(event.getPaid())
                .title(event.getTitle())
                .build();

    }
    public static Event toEvent(NewEventDto newEventDto, Categories categories, Location location, User user) {
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .categories(categories)
                .description(newEventDto.getDescription())
                .eventDate(newEventDto.getEventDate())
                .location(location)
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.getRequestModeration())
                .title(newEventDto.getTitle())
                .initiator(user)
                .state(State.PENDING)
                .build();
    }
}

