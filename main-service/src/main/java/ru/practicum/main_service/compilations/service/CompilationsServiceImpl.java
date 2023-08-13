package ru.practicum.main_service.compilations.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.main_service.compilations.dto.CompilationsResponseDto;
import ru.practicum.main_service.compilations.model.Compilations;
import ru.practicum.main_service.compilations.repository.CompilationsRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompilationsServiceImpl implements CompilationsService {
    private CompilationsRepository compilationsRepository;
    @Override
    public List<CompilationsResponseDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        int offset = from > 0 ? from / size : 0;
        PageRequest page = PageRequest.of(offset, size);
        List<Compilations> compilations;
        


        return null;
    }
}
