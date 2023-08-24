package ru.practicum.main_service.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main_service.event.model.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
