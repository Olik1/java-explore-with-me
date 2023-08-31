package ru.practicum.main_service.locations;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main_service.locations.model.Location;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findByLatAndLon(Float lat, Float lon);

    Optional<Location> findByLatAndLonAndName(Float lat, Float lon, String name);

    boolean existsLocationByName(String name);

    boolean existsLocationByLatAndLon(Float lat, Float lon);
}
