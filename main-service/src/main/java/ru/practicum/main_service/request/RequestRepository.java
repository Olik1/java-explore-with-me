package ru.practicum.main_service.request;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main_service.request.model.Request;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByRequesterId(Long id);
//    Long countRequestsByEventIdAndStatusPublishedIsLike(Long eventId);
//Long countRequestByEventIdAndStatus(long id);
//    int countConfirmedRequestsByEventId(long id);


}
