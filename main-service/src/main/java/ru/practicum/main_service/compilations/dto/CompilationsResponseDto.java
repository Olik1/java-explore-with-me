package ru.practicum.main_service.compilations.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.main_service.events.model.Events;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompilationsResponseDto {
    private Long id;
    //Закреплена ли подборка на главной странице сайта
    private Boolean isPinned;
    private String title;
    private List<Events> events;


}
