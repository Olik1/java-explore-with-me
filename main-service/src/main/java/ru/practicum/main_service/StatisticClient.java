package ru.practicum.main_service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.practicum.HitRequestDto;
import ru.practicum.StatsResponseDto;
import ru.practicum.client.StatsClient;
import ru.practicum.main_service.event.dto.EventFullDto;
import ru.practicum.main_service.event.dto.EventShortDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public EventFullDto setViewsNumber(EventFullDto event) {
        List<StatsResponseDto> hits = statsClient.getStatistic(event.getCreatedOn(), LocalDateTime.now(),
                List.of("/events/" + event.getId()), true);
        if (!hits.isEmpty()) {
            event.setViews(hits.get(0).getHits());
        } else {
            event.setViews(0L);
        }
        return event;
    }
    public List<EventShortDto> setViewsNumber(List<EventShortDto> events) {
        List<String> uris = new ArrayList<>();
        for (EventShortDto eventShortDto : events) {
            uris.add("/events/" + eventShortDto.getId());
        }

        EventShortDto eventss = new EventShortDto();
        List<StatsResponseDto> hits = statsClient.getStatistic(LocalDateTime.now().minusYears(100),
                LocalDateTime.now(), uris, true);
        if (!hits.isEmpty()) {
            Map<Long, Integer> hitMap = mapHits(hits);
            for (EventShortDto event : events) {
                event.setViews(hitMap.getOrDefault(event.getId(), 0));
            }
        } else {
            for (EventShortDto event : events) {
                event.setViews(0);
            }
        }
        return events;
    }
    private Map<Long, Integer> mapHits(List<StatsResponseDto> hits) {
        Map<Long, Integer> hitMap = new HashMap<>();
        for (StatsResponseDto hit : hits) {
            String hitUri = hit.getUri();
            Long id = Long.valueOf(hitUri.substring(8));
            hitMap.put(id, (int) hit.getHits());
        }
        return hitMap;
    }
}
