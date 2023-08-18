package ru.practicum.main_service.compilations.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.main_service.compilations.model.Compilation;
import ru.practicum.main_service.events.dto.EventShortDto;
import ru.practicum.main_service.events.model.Events;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class CompilationMapper {
    public static Compilation toCompilation (NewCompilationDto newCompilationDto, List<Events> events) {
        return Compilation.builder()
                .isPinned(newCompilationDto.getIsPinned())
                .title(newCompilationDto.getTitle())
                .events(events)
                .build();

    }
    public static CompilationDto toCompilationDto (Compilation compilation, List<EventShortDto> events) {
        return CompilationDto.builder()
                .isPinned(compilation.getIsPinned())
                .title(compilation.getTitle())
                .events(events)
                .build();

    }
}
