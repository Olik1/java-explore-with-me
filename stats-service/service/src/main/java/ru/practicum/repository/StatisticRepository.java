package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.model.StatsModel;
import ru.practicum.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticRepository extends JpaRepository<StatsModel, Long> {
    @Query("select new ru.practicum.model.ViewStats(e.app, e.uri, count(e.ip)) " +
            "from StatsModel e " +
            "where e.timestamp between :start and :end " +
            "group by e.app, e.uri " +
            "order by count(e.ip) desc"
    )
    List<ViewStats> findAllByDateBetween(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("select new ru.practicum.model.ViewStats(e.app, e.uri, count(distinct e.ip)) " +
            "from StatsModel e " +
            "where e.timestamp between :start and :end " +
            "group by e.app, e.uri " +
            "order by count(e.ip) desc"
    )
    List<ViewStats> findAllByDateBetweenUnique(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("select new ru.practicum.model.ViewStats(e.app, e.uri, count(e.ip)) " +
            "from StatsModel e " +
            "where e.timestamp between :start and :end " +
            "and e.uri in :uris " +
            "group by e.app, e.uri " +
            "order by count(e.ip) desc"
    )
    List<ViewStats> findAllByDateBetween(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("uris") List<String> uri
    );

    @Query("select new ru.practicum.model.ViewStats(e.app, e.uri, count(distinct e.ip)) " +
            "from StatsModel e " +
            "where e.timestamp between :start and :end " +
            "and e.uri in :uris " +
            "group by e.app, e.uri " +
            "order by count(e.ip) desc"
    )
    List<ViewStats> findAllByDateBetweenUnique(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("uris") List<String> uri
    );


}

