package ru.practicum.main_service.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
//import ru.practicum.client.StatsClient;
import ru.practicum.main_service.StatisticClient;
import ru.practicum.main_service.categories.model.Categories;
import ru.practicum.main_service.categories.repository.CategoriesRepository;
import ru.practicum.main_service.event.dto.*;
import ru.practicum.main_service.event.dto.mapper.EventsMapper;
import ru.practicum.main_service.event.dto.mapper.LocationMapper;
import ru.practicum.main_service.event.model.Event;
import ru.practicum.main_service.event.model.Location;
import ru.practicum.main_service.event.model.SortEvents;
import ru.practicum.main_service.event.repository.EventsRepository;
import ru.practicum.main_service.exception.ConflictException;
import ru.practicum.main_service.exception.ObjectNotFoundException;
import ru.practicum.main_service.request.RequestRepository;
import ru.practicum.main_service.request.dto.ParticipationRequestDto;
import ru.practicum.main_service.request.dto.RequestMapper;
import ru.practicum.main_service.request.model.ParticipationRequestStatus;
import ru.practicum.main_service.request.model.Request;
import ru.practicum.main_service.event.model.State;
import ru.practicum.main_service.users.model.User;
import ru.practicum.main_service.users.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventsServiceImpl implements EventsService {
    private final EventsRepository eventsRepository;
    private final UserRepository userRepository;
    private final StatisticClient statsClient;
    private final CategoriesRepository categoriesRepository;
    private final RequestRepository requestRepository;


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
    public List<EventsShortDto> getEvents(String text, List<Long> categories, Boolean paid,
                                          LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                          Boolean onlyAvailable, SortEvents sort, Integer from, Integer size) {

        //TODO service statistic + count views

        String lowerCase = text.toLowerCase();
        PageRequest page;
        List<Event> eventList;
        if (sort.equals(SortEvents.EVENT_DATE)) {
            page = PageRequest.of(from, size, Sort.by("eventDate"));
        } else {
            page = PageRequest.of(from, size);
        }
        eventList = eventsRepository.findAll();
        if (onlyAvailable.equals(true)) {
//            eventsList = eventsRepository;
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
     * событие должно быть опубликовано
     * информация о событии должна включать в себя количество просмотров и
     * количество подтвержденных запросов
     * информацию о том, что по этому эндпоинту был осуществлен и обработан запрос,
     * нужно сохранить в сервисе статистики
     */
    @Override
    public EventsFullDto getEventById(Long eventId, String ip) {
        //TODO service statistic + count views
        Event event = eventsRepository.findByIdAndAndState(eventId, State.PUBLISHED) //событие должно быть опубликовано
                .orElseThrow(() -> new ObjectNotFoundException("Не найдено опубликованное событие"));
        statsClient.saveHit("/events/" + eventId, ip);
        Long view = statsClient.getViewsByEventId(eventId);
        return EventsMapper.toEventFullDto(event);
    }

    @Override
    public List<EventsFullDto> getAllEventsByUserId(Long userId, Integer from, Integer size) {
        int offset = from > 0 ? from / size : 0;
        PageRequest page = PageRequest.of(offset, size);
        List<Event> events = eventsRepository.findByInitiatorId(userId, page);
        return events.stream().map(EventsMapper::toEventFullDto).collect(Collectors.toList());
    }

    @Override
    public EventsFullDto createEvents(Long userId, NewEventDto newEventDto) {
        //если в пре-модерация заявок на участие ничего нет, то устанавливаем true
        if (newEventDto.getRequestModeration() == null) {
            newEventDto.setRequestModeration(true);
        }
        LocalDateTime time = newEventDto.getEventDate();
        if (time.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ConflictException("Cобытие не может быть раньше, чем через два часа от текущего момента!");
        }
        User user = getUser(userId);
        Location location = LocationMapper.toLocation(newEventDto.getLocation());
        Categories categories = getCategoriesIfExist(newEventDto.getCategory());
        Event event = EventsMapper.toEvent(newEventDto, categories, location, user);
        Event result = eventsRepository.save(event);
        return EventsMapper.toEventFullDto(result);
    }

    @Override
    public EventsFullDto getEventsByUserId(Long userId, Long eventId) {
        Event event = eventsRepository.findByInitiatorIdAndId(userId, eventId).orElseThrow(
                () -> new ObjectNotFoundException("событие не найдено у пользователя!"));

        return EventsMapper.toEventFullDto(event);
    }

    @Override
    public EventsFullDto updateEventsByUser(Long userId, Long eventId, UpdateEventUserRequestDto requestDto) {
        Event event = getEvents(eventId);
        if (event.getState().equals(State.PENDING)) {
            throw new ConflictException("Изменить можно только отмененные события или события в состоянии ожидания модерации!");
        }
        updateEvents(event, requestDto);
        if (requestDto != null) {
            switch (requestDto.getStateAction()) {
                case CANCEL_REVIEW:
                    event.setState(State.CANCELED);
                    break;
                case SEND_TO_REVIEW:
                    event.setState(State.PENDING);
                    event.setPublishedOn(LocalDateTime.now());
            }
        }
        Event result = eventsRepository.save(event);

        return EventsMapper.toEventFullDto(result);
    }

    @Override
    public List<ParticipationRequestDto> getRequestUserEvents(Long userId, Long eventId) {
        User user = getUser(userId);
        Event event = getEvents(eventId);

        if (!user.getId().equals(event.getInitiator().getId())) {
            throw new ConflictException("Пользователь не инициатор события!");
        }
        if (event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("Изменить можно только отмененные события или события в состоянии ожидания модерации!");
        }

        List<Request> requests = requestRepository.findByEventId(eventId);
        return requests.stream().map(RequestMapper::toRequestDto).collect(Collectors.toList());
    }

    /*
    если для события лимит заявок равен 0 или отключена пре-модерация заявок, то подтверждение заявок не требуется
    нельзя подтвердить заявку, если уже достигнут лимит по заявкам на данное событие (Ожидается код ошибки 409)
    статус можно изменить только у заявок, находящихся в состоянии ожидания (Ожидается код ошибки 409)
    если при подтверждении данной заявки, лимит заявок для события исчерпан, то все неподтверждённые заявки необходимо отклонить
     */
    @Override
    public EventRequestStatusUpdateResult updateStatusRequestByUserIdForEvents(Long userId, Long eventId,
        EventRequestStatusUpdateRequest requestStatusUpdate) {
        User user = getUser(userId);
        Event event = getEvents(eventId);

//        EventRequestStatusUpdateResult request = EventRequestStatusUpdateRequest.builder().build();
        if (!user.getId().equals(event.getInitiator().getId())) {
            throw new ConflictException("Пользователь не инициатор события!");
        }
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            throw new ConflictException("Не требуется модерация и подтверждения заявок");
        }
        if (!event.getState().equals(State.PENDING)) {
            throw new ConflictException("Статус можно изменить только у заявок, находящихся в состоянии ожидания!");
        }
        Long confirmedRequests = requestRepository.countAllByEventIdAndStatus(eventId, ParticipationRequestStatus.CONFIRMED);
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() <= (confirmedRequests)) {
            throw new ConflictException("Нельзя подтвердить заявку, если уже достигнут лимит по заявкам на данное событие!");
        }
        List<Request> requestsToUpdate = requestRepository.findAllByIdIn(requestStatusUpdate.getRequestIds());
        List<Request> confirmed = new ArrayList<>();
        List<Request> rejected = new ArrayList<>();

        for (Request request : requestsToUpdate) {
            if (!request.getEvent().getId().equals(eventId)) {
                rejected.add(request);
                continue;
            }
            if(requestStatusUpdate.getStatus().equals("CONFIRMED")) {
                if (confirmedRequests < event.getParticipantLimit()) {
                    request.setStatus(ParticipationRequestStatus.CONFIRMED);
                    confirmedRequests++;
                    confirmed.add(request);
                } else {
                    request.setStatus(ParticipationRequestStatus.REJECTED);
                    rejected.add(request);
                }

            } else {
                request.setStatus(ParticipationRequestStatus.REJECTED);
                rejected.add(request);
            }
        }
        eventsRepository.save(event);
        requestRepository.saveAll(requestsToUpdate);

        return RequestMapper.toUpdateResultDto(confirmed, rejected);
    }

    private void updateEvents(Event event, UpdateEventUserRequestDto requestDto) {
        if (requestDto.getAnnotation() != null) {
            event.setAnnotation(requestDto.getAnnotation());
        }
        if (requestDto.getCategory() != null) {
            Categories categories = getCategoriesIfExist(requestDto.getCategory());
            event.setCategories(categories);
        }
        if (requestDto.getDescription() != null) {
            event.setDescription(requestDto.getDescription());
        }
        if (requestDto.getEventDate() != null) {
            event.setEventDate(requestDto.getEventDate());
        }
        if (event.getLocation() != null) {
            event.setLocation(LocationMapper.toLocation(requestDto.getLocation()));
        }
        if (event.getPaid() != null) {
            event.setPaid(requestDto.getPaid());
        }
        if (event.getParticipantLimit() != null) {
            event.setParticipantLimit(requestDto.getParticipantLimit());
        }
        if (event.getRequestModeration() != null) {
            event.setRequestModeration(requestDto.getRequestModeration());
        }
        if (event.getTitle() != null) {
            event.setTitle(requestDto.getTitle());
        }
        if (event.getTitle() != null) {
            event.setTitle(requestDto.getTitle());
        }
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Такого пользователя не существует!"));
    }

    private Event getEvents(Long eventId) {
        return eventsRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("Такого мероприятия не существует!"));
    }

    public Categories getCategoriesIfExist(Long catId) {
        return categoriesRepository.findById(catId).orElseThrow(
                () -> new ObjectNotFoundException("Не найдена выбранная категория"));
    }
}
