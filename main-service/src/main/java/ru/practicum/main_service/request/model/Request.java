package ru.practicum.main_service.request.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.main_service.event.model.Event;
import ru.practicum.main_service.users.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "requests")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Request {
    /**
     * Идентификатор
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Дата и время создания заявки
     */
    @Column(nullable = false)
    private LocalDateTime created;
    /**
     * Дата и время создания заявки
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ParticipationRequestStatus status;

    @ManyToOne
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

}
