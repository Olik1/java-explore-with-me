package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.HitRequestDto;
import ru.practicum.StatsResponseDto;
import ru.practicum.mapper.StatsMapper;
import ru.practicum.model.ViewStats;
import ru.practicum.repository.StatisticRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {
    private final StatisticRepository statisticRepository;
    public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * Сохранение информации о том, что на uri конкретного сервиса был отправлен запрос пользователем.
     * Название сервиса, uri и ip пользователя указаны в теле запроса.
     */
    @Override
    public void postHit(HitRequestDto hitRequestDto) {
        statisticRepository.save(StatsMapper.toStats(hitRequestDto));
    }

    /**
     * start  - дата и время начала диапазона за который нужно выгрузить статистику
     * end    - дата и время конца диапазона за который нужно выгрузить статистику
     * список статистики по посещениям
     * uris   - список uri для которых нужно выгрузить статистику
     * unique - нужно ли учитывать только уникальные ip
     */
    @Override
    public List<StatsResponseDto> getStatistics(String start, String end, List<String> uris, Boolean unique) {
        List<ViewStats> viewStatsList = new ArrayList<>();
        List<StatsResponseDto> statsResponseDtos = new ArrayList<>();;
        LocalDateTime startTime = LocalDateTime.parse(start);//, DateTimeFormatter.ofPattern(TIME_FORMAT));
        LocalDateTime endTime = LocalDateTime.parse(end);//, DateTimeFormatter.ofPattern(TIME_FORMAT));
        if (uris == null || uris.isEmpty()) {
            if(unique) {
                viewStatsList = statisticRepository.findAllByDateBetweenUnique(startTime, endTime);
            } else {
                viewStatsList = statisticRepository.findAllByDateBetween(startTime, endTime);
            }
        } else {
            if (unique) {
                viewStatsList = statisticRepository.findAllByDateBetweenUnique(startTime, endTime, uris);
            } else {
                viewStatsList = statisticRepository.findAllByDateBetween(startTime, endTime, uris);
            }

        }


        for (ViewStats viewStats : viewStatsList) {
            statsResponseDtos.add(StatsMapper.toStatsResponseDto(viewStats));
        }
        return statsResponseDtos;



        // 1. если список пуст возвращаем статистику между start и end
        // 2. если не пуст смотрим уникальны ли ip, если да возвращаем статистику по уникальным ip, если нет общую статистику
    }
}
