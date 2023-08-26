package ru.practicum.main_service.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.main_service.StatisticClient;
import ru.practicum.main_service.categories.model.Categories;
import ru.practicum.main_service.categories.repository.CategoriesRepository;
import ru.practicum.main_service.event.dto.*;
import ru.practicum.main_service.event.dto.mapper.EventMapper;
import ru.practicum.main_service.event.model.Event;
import ru.practicum.main_service.event.model.Location;
import ru.practicum.main_service.event.model.SortEvents;
import ru.practicum.main_service.event.model.State;
import ru.practicum.main_service.event.repository.CustomBuiltEventRepository;
import ru.practicum.main_service.event.repository.EventRepository;
import ru.practicum.main_service.event.repository.LocationRepository;
import ru.practicum.main_service.exception.ConflictException;
import ru.practicum.main_service.exception.ObjectNotFoundException;
import ru.practicum.main_service.request.RequestRepository;
import ru.practicum.main_service.request.dto.ParticipationRequestDto;
import ru.practicum.main_service.request.dto.RequestMapper;
import ru.practicum.main_service.request.model.ParticipationRequestStatus;
import ru.practicum.main_service.request.model.Request;
import ru.practicum.main_service.users.model.User;
import ru.practicum.main_service.users.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final StatisticClient statsClient;
    private final CategoriesRepository categoriesRepository;
    private final RequestRepository requestRepository;
    private final CustomBuiltEventRepository customBuiltEventRepository;
    private final LocationRepository locationRepository;


    @Override
    public List<EventShortDto> getEvents(String text, List<Long> categories, Boolean paid,
                                         LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                         Boolean onlyAvailable, SortEvents sort, Integer from, Integer size,
                                         HttpServletRequest request) {

        if (rangeStart != null && rangeEnd != null) {
            if (rangeStart.isAfter(rangeEnd)) {
                throw new ValidationException(String.format("Дата начала %s позже даты завершения %s.", rangeStart, rangeEnd));
            }
        }

        CriteriaPublic criteria = CriteriaPublic.builder()
                .text(text)
                .categories(categories)
                .paid(paid)
                .onlyAvailable(onlyAvailable)
                .sort(sort)
                .from(from)
                .size(size)
                .build();
        String ip = request.getRemoteAddr();
        String uri = request.getRequestURI();
        List<Event> events = customBuiltEventRepository.findEventsPublic(criteria);

        List<EventShortDto> result = events.stream().map(EventMapper::mapToShortDto).collect(Collectors.toList());

        if (result.size() > 0) {
            statsClient.setViewsNumber(result);

            for (EventShortDto event : result) {
                event.setConfirmedRequests(requestRepository.countAllByEventIdAndStatus(event.getId(),
                        ParticipationRequestStatus.CONFIRMED));
            }
        }

        statsClient.saveHit(uri, ip);

        if (result.size() > 0) {
            for (EventShortDto event : result) {
                statsClient.saveHit("/events/" + event.getId(), ip);
            }
        } else {
            return new ArrayList<EventShortDto>();
        }
        if (criteria.getSort() == SortEvents.VIEWS) {
            return result.stream().sorted(Comparator.comparingInt(EventShortDto::getViews)).collect(Collectors.toList());
        }

        return result.stream().sorted(Comparator.comparing(EventShortDto::getEventDate)).collect(Collectors.toList());

    }

    @Override
    public EventFullDto getEventById(Long eventId, HttpServletRequest request) {
        Event event = eventRepository.findByIdAndAndState(eventId, State.PUBLISHED)
                .orElseThrow(() -> new ObjectNotFoundException("Не найдено опубликованное событие"));
        String ip = request.getRemoteAddr();
        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
        statsClient.saveHit("/events/" + eventId, ip);
        statsClient.setViewsNumber(eventFullDto);
        eventFullDto.setConfirmedRequests(requestRepository.countAllByEventIdAndStatus(event.getId(),
                ParticipationRequestStatus.CONFIRMED));

        return eventFullDto;
    }

    @Override
    public List<EventFullDto> getAllEventsByUserId(Long userId, Integer from, Integer size) {
        int offset = from > 0 ? from / size : 0;
        PageRequest page = PageRequest.of(offset, size);
        List<Event> events = eventRepository.findByInitiatorId(userId, page);
        return events.stream().map(EventMapper::toEventFullDto).collect(Collectors.toList());
    }

    @Override
    public EventFullDto createEvents(Long userId, NewEventDto newEventDto) {
        //если в пре-модерация заявок на участие ничего нет, то устанавливаем true
        if (newEventDto.getRequestModeration() == null) {
            newEventDto.setRequestModeration(true);
        }
        LocalDateTime eventDate = newEventDto.getEventDate();
        if (eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ConflictException("Cобытие не может быть раньше, чем через два часа от текущего момента!");
        }
        User user = getUser(userId);
        Location location = getLocation(newEventDto.getLocation());
        locationRepository.save(location);
        Categories categories = getCategoriesIfExist(newEventDto.getCategory());
        Event event = EventMapper.toEvent(newEventDto, categories, location, user);
        event.setConfirmedRequests(0L);
        event.setViews(0L);
        event.setCreatedOn(LocalDateTime.now());
        Event result = eventRepository.save(event);
        return EventMapper.toEventFullDto(result);
    }

    @Override
    public EventFullDto getEventsByUserId(Long userId, Long eventId) {
        Event event = eventRepository.findByInitiatorIdAndId(userId, eventId).orElseThrow(
                () -> new ObjectNotFoundException("событие не найдено у пользователя!"));
        return EventMapper.toEventFullDto(event);
    }

    @Override
    public EventFullDto updateEventsByUser(Long userId, Long eventId, UpdateEventRequestDto requestDto) {

        Event event = getEvents(eventId);
        if (event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("Изменить можно только отмененные события или события в состоянии ожидания модерации!");
        }
        updateEvents(event, requestDto);

        if (requestDto.getStateAction() != null) {

            switch (requestDto.getStateAction()) {
                case CANCEL_REVIEW:
                    event.setState(State.CANCELED);
                    break;
                case SEND_TO_REVIEW:
                    event.setState(State.PENDING);
                    event.setPublishedOn(LocalDateTime.now());
            }
        }
        Event toUpdate = eventRepository.save(event);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(toUpdate);
        eventFullDto.setConfirmedRequests(requestRepository.countAllByEventIdAndStatus(event.getId(),
                ParticipationRequestStatus.CONFIRMED));
        statsClient.setViewsNumber(eventFullDto);
        return eventFullDto;
    }

    @Override
    public List<ParticipationRequestDto> getRequestUserEvents(Long userId, Long eventId) {
        User user = getUser(userId);
        Event event = getEvents(eventId);

        if (!user.getId().equals(event.getInitiator().getId())) {
            throw new ConflictException("Пользователь не инициатор события!");
        }

        List<Request> requests = requestRepository.findByEventId(eventId);
        return requests.stream().map(RequestMapper::toRequestDto).collect(Collectors.toList());
    }

    @Override
    public EventRequestStatusUpdateResult updateStatusRequestByUserIdForEvents(Long userId, Long eventId,
                                                                               EventRequestStatusUpdateRequest requestStatusUpdate) {
        User user = getUser(userId);
        Event event = getEvents(eventId);

        if (!user.getId().equals(event.getInitiator().getId())) {
            throw new ConflictException("Пользователь не инициатор события!");
        }
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            throw new ConflictException("Не требуется модерация и подтверждения заявок");
        }

        Long confirmedRequests = requestRepository.countAllByEventIdAndStatus(eventId, ParticipationRequestStatus.CONFIRMED);
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() <= (confirmedRequests)) {
            throw new ConflictException("Нельзя подтвердить заявку, если уже достигнут лимит по заявкам на данное событие!");
        }
        List<Request> requestsToUpdate = requestRepository.findAllByIdIn(requestStatusUpdate.getRequestIds());
        List<Request> confirmed = new ArrayList<>();
        List<Request> rejected = new ArrayList<>();

        for (Request request : requestsToUpdate) {

            if (!request.getStatus().equals(ParticipationRequestStatus.PENDING)) {
                continue;
            }


            if (!request.getEvent().getId().equals(eventId)) {
                rejected.add(request);
                continue;
            }
            if (requestStatusUpdate.getStatus().equals("CONFIRMED")) {
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
        eventRepository.save(event);
        requestRepository.saveAll(requestsToUpdate);

        return RequestMapper.toUpdateResultDto(confirmed, rejected);
    }

    @Override
    public List<EventFullDto> adminGetEvents(List<Long> userIds, List<State> states, List<Long> categories, String rangeStart, String rangeEnd, Integer from, Integer size) {
        LocalDateTime start = null;
        LocalDateTime end = null;
        if (rangeStart != null) {
            start = LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        if (rangeEnd != null) {
            end = LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }

        if (start != null && end != null) {
            if (start.isAfter(end)) {
                throw new ValidationException("Дата начала  не может быть после даты завершения.");
            }
        }
        Criteria criteria = Criteria.builder()
                .users(userIds)
                .states(states)
                .categories(categories)
                .from(from)
                .size(size)
                .rangeStart(start)
                .rangeEnd(end)
                .build();
        List<Event> events = customBuiltEventRepository.getEvents(criteria);
        var result = events.stream().map(EventMapper::toEventFullDto)
                .map(statsClient::setViewsNumber).collect(Collectors.toList());

        return result;
    }

    @Override
    public EventFullDto adminUpdateEvent(Long eventId, UpdateEventRequestDto requestDto) {
        Event event = getEvents(eventId);

        if (requestDto.getEventDate() != null && event.getPublishedOn() != null && requestDto.getEventDate().isBefore(event.getPublishedOn().plusHours(1))) {
            throw new ValidationException("дата начала изменяемого события должна быть не ранее чем за час от даты публикации");
        }
        if (requestDto.getStateAction() != null) {


            switch (requestDto.getStateAction()) {
                case PUBLISH_EVENT:
                    if (event.getState() != State.PENDING) {
                        throw new ConflictException("Состояние события должно быть PENDING");
                    }
                    event.setState(State.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                    break;
                case REJECT_EVENT:
                    if (event.getState() == State.PUBLISHED) {
                        throw new ConflictException("Невозможно отменить опубликованное мероприятие");
                    }
                    event.setState(State.CANCELED);
                    break;
                case SEND_TO_REVIEW:
                case CANCEL_REVIEW:
                    if (event.getState() == State.PUBLISHED) {
                        throw new ConflictException("Состояние события должно быть на ожидании или отмененным");
                    }
                    break;
            }
        }
        updateEvents(event, requestDto);
        Event toUpdate = eventRepository.save(event);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(toUpdate);
        eventFullDto.setConfirmedRequests(requestRepository.countAllByEventIdAndStatus(event.getId(),
                ParticipationRequestStatus.CONFIRMED));
        statsClient.setViewsNumber(eventFullDto);
        return eventFullDto;
    }

    private void updateEvents(Event event, UpdateEventRequestDto requestDto) {
        if (requestDto.getAnnotation() != null) {
            event.setAnnotation(requestDto.getAnnotation());
        }
        if (requestDto.getCategory() != null) {
            Categories categories = getCategoriesIfExist(requestDto.getCategory());
            event.setCategory(categories);
        }
        if (requestDto.getDescription() != null) {
            event.setDescription(requestDto.getDescription());
        }
        if (requestDto.getEventDate() != null) {
            event.setEventDate(requestDto.getEventDate());
        }
        if (requestDto.getLocation() != null) {
            Location location = getLocation(requestDto.getLocation());
            event.setLocation(location);
        }
        if (requestDto.getPaid() != null) {
            event.setPaid(requestDto.getPaid());
        }
        if (requestDto.getParticipantLimit() != null) {
            event.setParticipantLimit(requestDto.getParticipantLimit());
        }
        if (requestDto.getRequestModeration() != null) {
            event.setRequestModeration(requestDto.getRequestModeration());
        }
        if (requestDto.getTitle() != null) {
            event.setTitle(requestDto.getTitle());
        }


    }

    private Location getLocation(LocationDto locationDto) {
        Optional<Location> location = locationRepository.findByLatAndLon(locationDto.getLat(), locationDto.getLon());

        Location savedLocation;
        if (location.isPresent()) {
            savedLocation = location.get();
            //Если список не пустой, просто берем первый элемент списка как сохраненную локацию
            log.info("Локация уже существует: {}.", savedLocation);
        } else {
            //локация с указанной lat и lon не существует, сохраняем новую локацию в бд
            savedLocation = locationRepository.save(new Location(locationDto.getLat(), locationDto.getLon()));
            log.info("Сохранение локации: {}.", savedLocation);
        }

        return savedLocation;
    }


    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Такого пользователя не существует!"));
    }

    private Event getEvents(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("Такого мероприятия не существует!"));
    }

    public Categories getCategoriesIfExist(Long catId) {
        return categoriesRepository.findById(catId).orElseThrow(
                () -> new ObjectNotFoundException("Не найдена выбранная категория"));
    }
}
