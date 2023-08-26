package ru.practicum.main_service.compilations.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationDto {
    private Boolean pinned = false;
    @NotBlank
    @Size(min = 1, max = 50)
    private String title;
    private List<Long> events = new ArrayList<>();

}
