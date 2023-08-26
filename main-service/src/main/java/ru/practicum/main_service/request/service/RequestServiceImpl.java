package ru.practicum.main_service.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.main_service.event.model.Event;
import ru.practicum.main_service.event.model.State;
import ru.practicum.main_service.event.repository.EventRepository;
import ru.practicum.main_service.exception.ConflictException;
import ru.practicum.main_service.exception.ObjectNotFoundException;
import ru.practicum.main_service.request.RequestRepository;
import ru.practicum.main_service.request.dto.ParticipationRequestDto;
import ru.practicum.main_service.request.dto.RequestMapper;
import ru.practicum.main_service.request.model.ParticipationRequestStatus;
import ru.practicum.main_service.request.model.Request;
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
    private final EventRepository eventRepository;

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
        Event event = getEventsById(eventId);
        Long confirmedRequestAmount = requestsRepository.countAllByEventIdAndStatus(eventId, ParticipationRequestStatus.CONFIRMED);
        // выбирает из Request, где поле `event.id` равно заданному `eventId` и поле `status` равно "CONFIRMED"
        if (user.getId().equals(event.getInitiator().getId())) {
            throw new ConflictException("инициатор события не может добавить запрос на участие в своём событии");
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("нельзя участвовать в неопубликованном событии!");
        }
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() <= confirmedRequestAmount) {
            throw new ConflictException("достигнут лимит запросов на участие!");
        }
        if (requestsRepository.existsRequestByRequester_IdAndEvent_Id(userId, eventId)) {
            throw new ConflictException("инициатор события не может добавить запрос на участие в своём событии");
        }

        Request request = Request.builder()
                .requester(user)
                .event(event)
                .created(LocalDateTime.now())
                .build();
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) { // нужна ли модерация на участие
            request.setStatus(ParticipationRequestStatus.CONFIRMED);
        } else {
            request.setStatus(ParticipationRequestStatus.PENDING);
        }
        log.info("Запрос create на добавление запроса от текущего пользователя на участие в событии, с ids: {}", userId);

        return RequestMapper.toRequestDto(requestsRepository.save(request));
    }

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        getUserById(userId);
        Request request = getRequestById(requestId);
        if (!userId.equals(request.getRequester().getId())) {
            throw new ConflictException("Можно отменить только свой запрос на участие!");
        }
        request.setStatus(ParticipationRequestStatus.CANCELED);
        log.info("Запрос PATH на отмену своего запроса на участие в событии, с ids: {} {}", userId, requestId);
        return RequestMapper.toRequestDto(requestsRepository.save(request));
    }

    public User getUserById(Long userid) {
        return userRepository.findById(userid).orElseThrow(
                () -> new ObjectNotFoundException("Не найден пользователь по id!"));

    }

    public Event getEventsById(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(
                () -> new ObjectNotFoundException("Не найдено мероприятие по id!"));
    }

    public Request getRequestById(Long requestId) {
        return requestsRepository.findById(requestId).orElseThrow(
                () -> new ObjectNotFoundException("Не найден запрос по id!"));
    }
}
