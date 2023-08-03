package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.HitRequestDto;
import ru.practicum.StatsResponseDto;
import ru.practicum.model.StatsModel;

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
    public static StatsResponseDto toStatsResponseDto(StatsModel stats) {
        return StatsResponseDto.builder()
                .app(stats.getApp())
                .uri(stats.getUri())
//                .hits(stats.g)
                .build();
    }
}
