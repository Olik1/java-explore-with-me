package ru.practicum.main_service.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.main_service.event.model.Event;
import ru.practicum.main_service.event.model.State;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByIdAndAndState(Long eventId, State state);

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

    //    Page<Event> findByInitiatorId(Long initiatorId, Pageable page);
    List<Event> findByInitiatorId(Long initiatorId, Pageable pageable);

    Optional<Event> findByInitiatorIdAndId(Long initiatorId, Long eventId);

    List<Event> findAllByIdIn(List<Long> ids);




}
