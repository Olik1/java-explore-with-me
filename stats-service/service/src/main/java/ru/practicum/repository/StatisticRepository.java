package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.model.StatsModel;
import ru.practicum.model.ViewStats;


import java.time.LocalDateTime;
import java.util.List;

public interface StatisticRepository extends JpaRepository<StatsModel, Long> {
//        @Query("select new ru.practicum.model.ViewStats(e.app, e.uri, count(e.id)) " +
//            "from StatsModel e " +
//            "where e.timestamp between :start and :end " +
//            "group by e.app, e.uri " +
//            "order by count(e.id) desc"
//    )
//    List<ViewStats> findAllByDateBetween(
//            @Param("start") LocalDateTime start,
//            @Param("end") LocalDateTime end
//    );
}



//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//import ru.practicum.model.Stats;
//import ru.practicum.model.ViewStats;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//
//
//@Repository
//public interface StatisticRepository extends JpaRepository<Stats, Long> {
//    @Query("select new ru.practicum.model.ViewStats(e.app, e.uri, count(e.id)) " +
//            "from Stats e " +
//            "where e.timestamp between :start and :end " +
//            "group by e.app, e.uri " +
//            "order by count(e.id) desc"
//    )
//    List<ViewStats> findAllByDateBetween(
//            @Param("start") LocalDateTime start,
//            @Param("end") LocalDateTime end
//    );
//
//
//
//
////    List<Stats> findAllByTimestampBetween(LocalDateTime start, LocalDateTime end);
////    List<Stats> findAllByTimestampBetweenAndIpIn(LocalDateTime start, LocalDateTime end, List<String> uris);
//
//
//    //    List<Stat> getNotUniqueIpStat(LocalDateTime start, LocalDateTime end, List<String> uris);
//    //
//    //    @Query(name = "GetUniqueIpStat", nativeQuery = true)
//    //    List<Stat> getUniqueIpStat(LocalDateTime start, LocalDateTime end, List<String> uris);
//
//}
