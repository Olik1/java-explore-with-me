package ru.practicum.service;

import ru.practicum.HitRequestDto;
import ru.practicum.StatsResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticService {
    void postHit(HitRequestDto hitRequestDto);
    List<StatsResponseDto> getStatistics(String start, String end, List<String> uris, Boolean unique);
}
