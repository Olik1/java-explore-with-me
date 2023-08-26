package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.HitRequestDto;
import ru.practicum.StatsResponseDto;
import ru.practicum.exception.ValidationException;
import ru.practicum.mapper.StatsMapper;
import ru.practicum.model.ViewStats;
import ru.practicum.repository.StatisticRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {
    private final StatisticRepository statisticRepository;

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
    public List<StatsResponseDto> getStatistics(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        List<ViewStats> viewStatsList;

        if (start.isAfter(end)) {
            throw new ValidationException("Время начала не может быть позднее даты конца диапазона!");
        }
        if (uris == null || uris.isEmpty()) {
            if (unique) {
                viewStatsList = statisticRepository.findAllStatsByUniqIp(start, end);
            } else {
                viewStatsList = statisticRepository.findAllByDateBetween(start, end);
            }
        } else {
            if (unique) {
                viewStatsList = statisticRepository.findStatsByUrisByUniqIp(start, end, uris);
            } else {
                viewStatsList = statisticRepository.findAllByDateBetween(start, end, uris);
            }

        }

        return viewStatsList.stream()
                .map(StatsMapper::toStatsResponseDto).collect(Collectors.toList());

    }
}
