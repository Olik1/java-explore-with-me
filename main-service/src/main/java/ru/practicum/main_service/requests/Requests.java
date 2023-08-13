package ru.practicum.main_service.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.main_service.events.Events;
import ru.practicum.main_service.users.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "requests")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Requests {
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
    private State status;

    @ManyToOne
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @ManyToOne
    @JoinColumn(name = "events_id", nullable = false)
    private Events event;

}
