package ru.practicum.main_service.compilations.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main_service.compilations.model.Compilations;

import java.util.List;

public interface CompilationsRepository extends JpaRepository<Compilations, Long> {
    List<Compilations> findAllByIsPinned(Boolean isPinned, Pageable pageable);
    
}
