package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "stats")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatsModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    //Идентификатор сервиса для которого записывается информация
    @Column
    private String app;
    //URI для которого был осуществлен запрос
    @Column
    private String uri;
    //IP-адрес пользователя, осуществившего запрос
    @Column
    private String ip;
    //Дата и время, когда был совершен запрос к эндпоинту
    @Column
    private LocalDateTime timestamp;

}
