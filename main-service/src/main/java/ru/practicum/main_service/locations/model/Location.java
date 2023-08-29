package ru.practicum.main_service.locations.model;

import lombok.*;

import javax.persistence.*;


@Entity
@Table(name = "locations")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Float lat;
    private Float lon;
    private String name;
    private Float radius;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private LocationStatus status;

    public Location(Float lat, Float lon) {
        this.lat = lat;
        this.lon = lon;
    }
}
