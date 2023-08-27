package ru.practicum.main_service.event.model;

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

    public Location(Float lat, Float lon) {
        this.lat = lat;
        this.lon = lon;
    }
}
