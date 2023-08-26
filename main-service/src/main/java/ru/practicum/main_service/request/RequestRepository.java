package ru.practicum.main_service.request;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main_service.request.model.ParticipationRequestStatus;
import ru.practicum.main_service.request.model.Request;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByRequesterId(Long id);
//    Long countRequestsByEventIdAndStatusPublishedIsLike(Long eventId);
//Long countRequestByEventIdAndStatus(long id);
//    int countConfirmedRequestsByEventId(long id);
//List<Request> findByEventInitiatorIdAndEventId(Long initiatorId, Long eventId);
    List<Request> findByEventId(Long eventId);
    Long countAllByEventIdAndStatus(Long eventId, ParticipationRequestStatus state);

    List<Request> findAllByIdIn(List<Long> ids);

    boolean existsRequestByRequester_IdAndEvent_Id(Long requesterId, Long eventId);

}
