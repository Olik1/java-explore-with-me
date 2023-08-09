package ru.practicum.controller.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.HitRequestDto;
import ru.practicum.StatsResponseDto;
import ru.practicum.controller.StatisticController;
import ru.practicum.service.StatisticService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ExtendWith(MockitoExtension.class)
class StatisticControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private StatisticService statisticService;
    @InjectMocks
    StatisticController statisticController;
    //set up вызов проверки
    private static final String HIT_URL = "/hit";
    private static final String STATS_URL = "/stats";
    private HitRequestDto hitRequestDto;
    private StatsResponseDto statsResponseDto;

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
        statsResponseDto = StatsResponseDto.builder()
                .app("app")
                .uri("uri")
                .hits(1L)
                .build();
    }

//    @SneakyThrows
//    @Test
//    void postEndpointHit_WhenStatusIsOk() {
//        String result = mockMvc.perform(MockMvcRequestBuilders.post(HIT_URL)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(hitRequestDto)))
//                .andExpect(status().isCreated())
//                .andDo(print())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//
//        verify(statisticService, times(1)).postHit(hitRequestDto);
//    }

    @Test
    @SneakyThrows
    void getStatistic_WhenStatusIsOk() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        mockMvc.perform(get(STATS_URL)
                        .param("start", start.format(formatter))
                        .param("end", end.format(formatter))
                        .param("uris", "/value", "/value1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(statisticService).getStatistics(any(), any(), anyList(), any());
    }

    @Test
    @SneakyThrows
    void getStatistic_whenEndIsNotExist_thenReturnedBadRequest() {
        mockMvc.perform(get(STATS_URL)
                        .param("start", "2023-01-01 00:11:00"))
                .andExpect(status().isBadRequest());

        verify(statisticService, never()).getStatistics(any(), any(), anyList(), any());
    }

    @Test
    @SneakyThrows
    void getStatistic_whenStartIsNotExist_thenReturnedBadRequest() {
        mockMvc.perform(get(STATS_URL)
                        .param("end", "2023-01-01 00:11:00"))
                .andExpect(status().isBadRequest());

        verify(statisticService, never()).getStatistics(any(), any(), anyList(), any());
    }

    @SneakyThrows
    @Test
    public void getStatistic_WhenUniqueValueIsNotValid_thenReturnedBadRequest() {
        String start = "2023-03-30 00:00:00";
        String end = "2024-03-00 00:00:00";

        mockMvc.perform(get("/stats")
                        .param("start", start)
                        .param("end", end)
                        .param("uris", "/value, /value1")
                        .param("unique", "ljdflJNDJ")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(statisticService, never()).getStatistics(any(), any(), anyList(), any());
    }
}
