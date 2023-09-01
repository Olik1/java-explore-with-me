package ru.practicum.main_service.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.main_service.event.model.Event;
import ru.practicum.main_service.event.model.State;

import java.util.List;
import java.util.Optional;


public interface EventRepository extends JpaRepository<Event, Long> {

    Optional<Event> findByIdAndAndState(Long eventId, State state);

    List<Event> findByInitiatorId(Long initiatorId, Pageable pageable);

    Optional<Event> findByInitiatorIdAndId(Long initiatorId, Long eventId);

    List<Event> findAllByIdIn(List<Long> ids);

    boolean existsEventsByCategory_Id(Long catId);

    @Query("SELECT e FROM Event e " +
            "WHERE FUNCTION('distance', :lat, :lon, e.location.lat, e.location.lon) " +
            "<= :radius " +
            "AND e.state = :state " +
            "ORDER BY e.eventDate DESC ")
    List<Event> findEventsWithLocationRadius(
            Float lat,
            Float lon,
            @Param("radius") Float radius,
            State state,
            Pageable pageable);


}
