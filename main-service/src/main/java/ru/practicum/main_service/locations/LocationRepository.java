package ru.practicum.main_service.locations;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main_service.locations.model.Location;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findByLatAndLonAndNameNull(Float lat, Float lon);

    boolean existsLocationByName(String name);

    boolean existsLocationByLatAndLon(Float lat, Float lon);
}
