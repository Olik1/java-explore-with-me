package ru.practicum;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HitRequestDto {
    @NotBlank(message = "Название приложения не может быть пустым")
    private String app;
    @NotBlank(message = "URI не может быть пустым")
    private String uri;
    @NotBlank(message = "IP пользователя не может быть пустым")
    private String ip;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "Время отправления запроса не может быть пустым")
    private LocalDateTime timestamp;
}
