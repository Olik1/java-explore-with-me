package ru.practicum.main_service.compilations.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.main_service.categories.dto.CategoriesMapper;
import ru.practicum.main_service.compilations.dto.CompilationDto;
import ru.practicum.main_service.compilations.dto.CompilationMapper;
import ru.practicum.main_service.compilations.dto.NewCompilationDto;
import ru.practicum.main_service.compilations.model.Compilation;
import ru.practicum.main_service.compilations.repository.CompilationRepository;
import ru.practicum.main_service.events.dto.EventShortDto;
import ru.practicum.main_service.events.dto.EventsMapper;
import ru.practicum.main_service.events.model.Events;
import ru.practicum.main_service.events.repository.EventsRepository;
import ru.practicum.main_service.users.dto.UserMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private CompilationRepository compilationsRepository;
    private EventsRepository eventsRepository;

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        int offset = from > 0 ? from / size : 0;
        PageRequest page = PageRequest.of(offset, size);
        List<Compilation> compilations = compilationsRepository.findAllByIsPinned(pinned, page);
        //создаем список с подборкой событий
        List<Events> eventList = new ArrayList<>();//список событий входящий в подборку
        for (Compilation compilation : compilations) {
            eventList.addAll(compilation.getEvents()); //из compilation вытягиваем все события
        }


        return null;
    }

    @Override
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        List<Events> events = eventsRepository.findAllById(newCompilationDto.getEvents());
        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto, events);
        Compilation result = compilationsRepository.save(compilation);
        log.info("Запрос POST на добавление новой подборки c id: {}", compilation.getId());
        List<EventShortDto> eventShortDto = events.stream().map(event ->
                EventsMapper.toEventShortDto(
                        event,
                        CategoriesMapper.toCategoryDto(event.getCategories()),
                        UserMapper.toUserDto(event.getInitiator())
                )).collect(Collectors.toList());
        return CompilationMapper.toCompilationDto(result, eventShortDto);
    }


}
