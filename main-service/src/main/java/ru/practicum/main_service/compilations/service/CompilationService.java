package ru.practicum.main_service.compilations.service;

import ru.practicum.main_service.compilations.dto.CompilationDto;
import ru.practicum.main_service.compilations.dto.NewCompilationDto;

import java.util.List;

public interface CompilationService {
    List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size);
    CompilationDto createCompilation(NewCompilationDto newCompilationDto);
}
