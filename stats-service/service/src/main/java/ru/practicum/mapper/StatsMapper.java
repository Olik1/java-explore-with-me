package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.HitRequestDto;
import ru.practicum.StatsResponseDto;
import ru.practicum.model.StatsModel;
import ru.practicum.model.ViewStats;

@UtilityClass
public class StatsMapper {
    public static StatsModel toStats(HitRequestDto endpointHitRequestDto) {
        return StatsModel.builder()
                .ip(endpointHitRequestDto.getIp())
                .timestamp(endpointHitRequestDto.getTimestamp())
                .uri(endpointHitRequestDto.getUri())
                .app(endpointHitRequestDto.getApp())
                .build();
    }

    public static StatsResponseDto toStatsResponseDto(ViewStats viewStats) {
        return StatsResponseDto.builder()
                .app(viewStats.getApp())
                .uri(viewStats.getUri())
                .hits(viewStats.getCount())
                .build();
    }
}
