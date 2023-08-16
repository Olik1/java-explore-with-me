package ru.practicum.main_service.compilations.service;

import ru.practicum.main_service.compilations.dto.CompilationsResponseDto;

import java.util.List;

public interface CompilationsService {
    List<CompilationsResponseDto> getCompilations( Boolean pinned, Integer from, Integer size);
}
