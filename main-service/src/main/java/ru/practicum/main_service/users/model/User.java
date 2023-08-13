package ru.practicum.main_service.users.model;
import javax.persistence.*;
import lombok.*;
@Data
@Entity
@Table(name = "users")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column(nullable = false, unique = true)
    private String email;
}
