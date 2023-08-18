package ru.practicum.main_service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.practicum.HitRequestDto;
import ru.practicum.StatsResponseDto;
import ru.practicum.client.StatsClient;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StatisticClient {

    private final StatsClient statsClient;
    private final static String APP = "main-service";

    public ResponseEntity<Object> saveHit(String uri, String ip) {
        HitRequestDto hitRequestDto = HitRequestDto.builder()
                .app(APP)
                .uri(uri)
                .ip(ip)
                .timestamp(LocalDateTime.now())
                .build();
        return statsClient.postEndpointHit(hitRequestDto);
    }

    public Long getViewsByEventId(Long eventId) {
        List<StatsResponseDto> hitsList = statsClient.getStatistic(LocalDateTime.now().minusDays(30),
                LocalDateTime.now(), List.of("/events/" + eventId), true);
        return !hitsList.isEmpty() ? hitsList.get(0).getHits() : 0L;
    }

}
