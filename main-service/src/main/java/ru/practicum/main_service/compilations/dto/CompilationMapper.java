package ru.practicum.main_service.compilations.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.main_service.compilations.model.Compilation;
import ru.practicum.main_service.event.dto.EventsShortDto;
import ru.practicum.main_service.event.model.Event;

import java.util.List;

@UtilityClass
public class CompilationMapper {
    public static Compilation toCompilation (NewCompilationDto newCompilationDto, List<Event> events) {
        return Compilation.builder()
                .pinned(newCompilationDto.getPinned())
                .title(newCompilationDto.getTitle())
                .events(events)
                .build();

    }
    public static CompilationDto toCompilationDto (Compilation compilation, List<EventsShortDto> events) {
        return CompilationDto.builder()
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .events(events)
                .build();

    }
}
