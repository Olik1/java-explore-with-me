package ru.practicum.main_service.compilations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main_service.compilations.model.Compilations;

import java.util.List;

public interface CompilationsRepository extends JpaRepository<CompilationsRepository, Long> {
//    List<Compilations> f
}
