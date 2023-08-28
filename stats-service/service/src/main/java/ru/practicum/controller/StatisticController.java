package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.HitRequestDto;
import ru.practicum.StatsResponseDto;
import ru.practicum.service.StatisticService;

import java.time.LocalDateTime;
import java.util.List;

@Validated
@RestController
@RequestMapping
@RequiredArgsConstructor
public class StatisticController {
    private final StatisticService statisticService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void postEndpointHit(@Validated @RequestBody HitRequestDto hitRequestDto) {
        statisticService.postHit(hitRequestDto);
    }

    @GetMapping("/stats")
    public List<StatsResponseDto> getStatistic(@RequestParam("start")
                                               @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                               @RequestParam("end")
                                               @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                               @RequestParam(name = "uris", required = false, defaultValue = "")
                                               List<String> uris,
                                               @RequestParam(name = "unique", required = false, defaultValue = "false")
                                               Boolean unique) {
        return statisticService.getStatistics(start, end, uris, unique);
    }

}
