package ru.practicum.main_service.events.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.main_service.events.model.Events;
import ru.practicum.main_service.request.model.State;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventsRepository extends JpaRepository<Events, Long> {
    Optional<Events> findByIdAndAndState(Long eventId, State state);

    //    @Query("select e from Events e " +
//            "where (UPPER(e.annotation) like UPPER(CONCAT('%',:text,'%')) " +
//            "or UPPER(e.description) like UPPER(CONCAT('%',:text,'%')) or :text is null ) " +
//            "and e.state = 'PUBLISHED' " +
//            "and ((:categories) is null or e.category.id in :categories) " +
//            "and e.paid = :paid " +
//            "and e.eventDate > :rangeStart " +
//            "and e.participants.size < e.participantLimit " +
//            "and e.eventDate <= :rangeEnd")
//    List<Events> getAvailableEventsForUser(
//            @Param("text") String text,
//            @Param("categories") List<Long> categories,
//            @Param("paid") Boolean paid,
//            @Param("rangeStart") LocalDateTime rangeStart,
//            @Param("rangeEnd") LocalDateTime rangeEnd,
//            Pageable pageable
//    );
//    @Query("select count(r.id) from Request r where r.event.id = ?1 AND r.status like 'CONFIRMED'")
//    Long findCountedRequestsByEventIdAndConfirmedStatus(Long eventId);
    @Query("select count(r.id) from Request r where r.event.id = ?1 AND r.status = 'CONFIRMED'")
    Long findCountedRequestsByEventIdAndConfirmedStatus(Long eventId);
    List<Events> findAllByIdIn(List<Long> ids);


}
