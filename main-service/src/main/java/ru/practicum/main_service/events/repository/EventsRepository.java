package ru.practicum.main_service.events.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main_service.events.model.Events;
import ru.practicum.main_service.requests.State;

import java.util.Optional;

public interface EventsRepository extends JpaRepository<Events, Long> {
    Optional<Events> findByIdAndAndState(Long eventId, State state);

}
