package ru.practicum.main_service.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.experimental.UtilityClass;
import ru.practicum.main_service.request.model.Request;
import ru.practicum.main_service.request.model.State;

import java.time.LocalDateTime;

@UtilityClass
public class RequestMapper {
    public static ParticipationRequestDto toRequestDto(Request request) {
        return ParticipationRequestDto.builder()
                .id(request.getId())
                .created(request.getCreated())
                .status(request.getStatus())
                .requester(request.getRequester().getId())
                .event(request.getEvent().getId())
                .build();
    }
}
