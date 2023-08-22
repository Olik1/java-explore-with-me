package ru.practicum.main_service.event.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.main_service.categories.model.Categories;
import ru.practicum.main_service.users.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "events")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    /**
     * Идентификатор
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Краткое описание
     */
    @Column(nullable = false, length = 2000)
    private String annotation;
    /**
     * Полное описание события
     */
    @Column(nullable = false, length = 7000)
    private String description;
    /**
     * Дата и время на которые намечено событие
     */
    @Column(nullable = false)
    private LocalDateTime eventDate;
    /**
     *
     * Широта и Долгота места проведения события
     */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;
    /**
     * Нужно ли оплачивать участие
     */
    @Column(nullable = false)
    private Boolean paid;
    /**
     * Краткое описание
     */
    @Column(nullable = false)
    private Long participantLimit;
    /**
     * Ограничение на количество участников
     */
    @Column(nullable = false)
    private Boolean requestModeration;
    /**
     * Заголовок
     */
    @Column(nullable = false, length = 120)
    private String title;
    /**
     * Список состояний жизненного цикла события
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private State state;
    /**
     * Краткое описание
     */
    private LocalDateTime createdOn;
    /**
     * Дата и время публикации события
     */
    private LocalDateTime publishedOn;
    /**
     * Пользователь создавший событие
     */
    @ManyToOne
    @JoinColumn(name = "initiator_id", nullable = false)
    private User initiator;
    /**
     *
     * Категория события
     */
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Categories category;
    @Transient
    private Long confirmedRequests;
    @Transient
    private Long views;
}
