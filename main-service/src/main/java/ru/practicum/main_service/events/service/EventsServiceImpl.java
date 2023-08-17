package ru.practicum.main_service.events.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
//import ru.practicum.client.StatsClient;
import ru.practicum.client.StatsClient;
import ru.practicum.main_service.StatisticClient;
import ru.practicum.main_service.categories.model.Categories;
import ru.practicum.main_service.events.dto.EventFullDto;
import ru.practicum.main_service.events.dto.EventShortDto;
import ru.practicum.main_service.events.dto.EventsMapper;
import ru.practicum.main_service.events.model.Events;
import ru.practicum.main_service.events.model.SortEvents;
import ru.practicum.main_service.events.repository.EventsRepository;
import ru.practicum.main_service.exception.ObjectNotFoundException;
import ru.practicum.main_service.requests.State;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventsServiceImpl implements EventsService {
    private final EventsRepository eventsRepository;
    private final StatisticClient statsClient;

    /**
     * в выдаче должны быть только опубликованные события;
     * текстовый поиск (по аннотации и подробному описанию) должен быть без учета регистра букв;
     * если в запросе не указан диапазон дат [rangeStart-rangeEnd], то нужно выгружать события,
     * которые произойдут позже текущей даты и времени;
     * информация о каждом событии должна включать в себя количество просмотров и количество
     * уже одобренных заявок на участие;
     * информацию о том, что по этому эндпоинту был осуществлен и обработан запрос,
     * нужно сохранить в сервисе статистики;
     */
    @Override
    public List<EventShortDto> getEvents(String text, List<Long> categories, Boolean paid,
                                         LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                         Boolean onlyAvailable, SortEvents sort, Integer from, Integer size) {

        //TODO service statistic + count views

        String lowerCase = text.toLowerCase();
        PageRequest page;
        List<Events> eventsList;
        if(sort.equals(SortEvents.EVENT_DATE)) {
        page = PageRequest.of(from, size, Sort.by("eventDate"));
        } else {
            page = PageRequest.of(from, size);
        }
        eventsList = eventsRepository.findAll();
        if(onlyAvailable.equals(true)) {
            eventsList = eventsRepository;
        }

        return null;
    }
/*
. В зависимости от значения sort происходит установка сортировки по полю eventDate
или оставляется сортировка по умолчанию.
6. В зависимости от значения onlyAvailable вызывается метод eventRepository.getAvailableEventsForUser
или eventRepository.getEventsForUser для получения списка событий в базе данных.
7. Если sort равен EventSort.VIEWS, вызывается метод sortEventsByViews для сортировки событий
по количеству просмотров.
8. Возвращается список events, который преобразуется с использованием метода parseToShortDtoWithMappers
в список объектов EventShortDto с помощью потока и коллектора Collectors.toList().
 */

    /**
     *
     * событие должно быть опубликовано
     * информация о событии должна включать в себя количество просмотров и
     * количество подтвержденных запросов
     * информацию о том, что по этому эндпоинту был осуществлен и обработан запрос,
     * нужно сохранить в сервисе статистики
     */
    @Override
    public EventFullDto getEventById(Long eventId, String ip) {
        //TODO service statistic + count views
        Events events = eventsRepository.findByIdAndAndState(eventId, State.PUBLISHED) //событие должно быть опубликовано
                .orElseThrow(() -> new ObjectNotFoundException("Не найдено опубликованное событие"));
        statsClient.saveHit("/events/" + eventId, ip);
        Long view = statsClient.getViewsByEventId(eventId);
        return EventsMapper.eventFullDto(events);
    }

}
