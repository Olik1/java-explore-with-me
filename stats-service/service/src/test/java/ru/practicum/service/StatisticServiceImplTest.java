package ru.practicum.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.HitRequestDto;
import ru.practicum.StatsResponseDto;
import ru.practicum.model.StatsModel;
import ru.practicum.model.ViewStats;
import ru.practicum.repository.StatisticRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatisticServiceImplTest {
    @Mock
    private StatisticRepository statisticRepository;
    @InjectMocks
    StatisticServiceImpl statisticService;
    private HitRequestDto hitRequestDto;
    @BeforeEach
    void beforeEach() {
        String param = "2023-08-04 13:37:00";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime timestamp = LocalDateTime.parse(param, dateTimeFormatter);
        hitRequestDto = HitRequestDto.builder()
                .app("app")
                .ip("ip")
                .uri("uri")
                .timestamp(timestamp)
                .build();
    }

    @Test
    void postHitWhenIsOk() {
        statisticService.postHit(hitRequestDto);
        verify(statisticRepository).save(any(StatsModel.class));

    }


    @Test
    void getStatistics() {
        // Arrange
        LocalDateTime start = LocalDateTime.of(2023, 8, 4, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2023, 8, 5, 0, 0, 0);
        List<String> uris = new ArrayList<>();
        Boolean unique = true;

        List<ViewStats> viewStatsList = new ArrayList<>();
        viewStatsList.add(new ViewStats("app1", "uri1", 1));
        viewStatsList.add(new ViewStats("app2", "uri2", 2));

        when(statisticRepository.findAllByDateBetweenUnique(start, end)).thenReturn(viewStatsList);

        List<StatsResponseDto> result = statisticService.getStatistics("2023-08-04 00:00:00", "2023-08-05 00:00:00", uris, unique);

        assertEquals(2, result.size());
        assertEquals("uri1", result.get(0).getUri());
        assertEquals("uri2", result.get(1).getUri());
    }
}