package ru.practicum.main_service.compilations.service;

import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.main_service.compilations.dto.CompilationsResponseDto;

import javax.validation.constraints.PositiveOrZero;
import java.util.List;

public interface CompilationsService {
    List<CompilationsResponseDto> getCompilations( Boolean pinned, Integer from, Integer size);
}
