package ru.practicum.main_service.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.main_service.events.model.Events;
import ru.practicum.main_service.events.repository.EventsRepository;
import ru.practicum.main_service.exception.ObjectNotFoundException;
import ru.practicum.main_service.exception.ConflictException;
import ru.practicum.main_service.request.RequestRepository;
import ru.practicum.main_service.request.dto.ParticipationRequestDto;
import ru.practicum.main_service.request.dto.RequestMapper;
import ru.practicum.main_service.request.model.Request;
import ru.practicum.main_service.request.model.State;
import ru.practicum.main_service.users.model.User;
import ru.practicum.main_service.users.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestsRepository;
    private final UserRepository userRepository;
    private final EventsRepository eventsRepository;

    @Override
    public List<ParticipationRequestDto> getRequest(Long userId) {
        getUserById(userId);
        List<Request> requestsList = requestsRepository.findByRequesterId(userId);
        log.info("Запрос GET на поиск о заявках текущего пользователя, с ids: {}", userId);
        return requestsList.stream().map(RequestMapper::toRequestDto).collect(Collectors.toList());
    }

    /*
    нельзя добавить повторный запрос (Ожидается код ошибки 409)
    инициатор события не может добавить запрос на участие в своём событии (Ожидается код ошибки 409)
    нельзя участвовать в неопубликованном событии (Ожидается код ошибки 409)
    если у события достигнут лимит запросов на участие - необходимо вернуть ошибку (Ожидается код ошибки 409)
    если для события отключена пре-модерация запросов на участие, то запрос должен автоматически перейти в состояние подтвержденного
     */
    @Override
    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        User user = getUserById(userId);
        Events event = getEventsById(eventId);
        Long limit = eventsRepository.findCountedRequestsByEventIdAndConfirmedStatus(eventId);
        // выбирает из Request, где поле `event.id` равно заданному `eventId` и поле `status` равно "CONFIRMED"
        if (user.getId().equals(event.getInitiator().getId())) {
            throw new ConflictException("инициатор события не может добавить запрос на участие в своём событии");
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("нельзя участвовать в неопубликованном событии!");
        }
        if (event.getParticipantLimit() >= limit) {
            throw new ConflictException("достигнут лимит запросов на участие!");
        }
        Request request = Request.builder()
                .requester(user)
                .event(event)
                .created(LocalDateTime.now())
                .build();
        if (!event.getRequestModeration() && limit == 0) { // нужна ли модерация на участие
            request.setStatus(State.CONFIRMED);
        } else {
            request.setStatus(State.PENDING);
        }
        log.info("Запрос create на добавление запроса от текущего пользователя на участие в событии, с ids: {}", userId);

        return RequestMapper.toRequestDto(requestsRepository.save(request));
    }

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        getUserById(userId);
        Request request = getRequestById(requestId);
        if(!userId.equals(request.getRequester().getId())) {
            throw new ConflictException("Можно отменить только свой запрос на участие!");
        }
        request.setStatus(State.CANCELED);
        log.info("Запрос PATH на отмену своего запроса на участие в событии, с ids: {} {}", userId, requestId);
        return RequestMapper.toRequestDto(requestsRepository.save(request));
    }

    public User getUserById(Long userid) {
        return userRepository.findById(userid).orElseThrow(
                () -> new ObjectNotFoundException("Не найден пользователь по id!"));

    }

    public Events getEventsById(Long eventId) {
        return eventsRepository.findById(eventId).orElseThrow(
                () -> new ObjectNotFoundException("Не найдено мероприятие по id!"));
    }
    public Request getRequestById(Long requestId) {
        return requestsRepository.findById(requestId).orElseThrow(
                () -> new ObjectNotFoundException("Не найден запрос по id!"));
    }
}