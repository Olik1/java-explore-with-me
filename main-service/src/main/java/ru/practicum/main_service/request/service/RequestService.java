package ru.practicum.main_service.request.service;

import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.main_service.request.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    List<ParticipationRequestDto> getRequest(@RequestParam Long userId);

    ParticipationRequestDto createRequest(Long userId, Long eventId);

    ParticipationRequestDto cancelRequest(Long userId, Long requestId);
}
