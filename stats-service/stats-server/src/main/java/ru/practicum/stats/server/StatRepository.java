package ru.practicum.stats.server;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.stats.server.model.Stat;
import ru.practicum.stats.server.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatRepository extends JpaRepository<Stat, Long> {

    @Query("""
            SELECT new ru.practicum.stats.server.model.ViewStats(s.app, s.uri, COUNT(s.ip))
            FROM Stat s
            WHERE s.timestamp BETWEEN :start AND :end
            AND (:uris IS NULL OR s.uri IN :uris)
            GROUP BY s.app, s.uri
            """)
    List<ViewStats> findViewStatsNotUniqueIp(@Param("start") LocalDateTime start,
                                                @Param("end") LocalDateTime end,
                                                @Param("uris") List<String> uris);

    @Query("""
            SELECT new ru.practicum.stats.server.model.ViewStats(s.app, s.uri, COUNT(DISTINCT s.ip))
            FROM Stat s
            WHERE s.timestamp BETWEEN :start AND :end
            AND (:uris IS NULL OR s.uri IN :uris)
            GROUP BY s.app, s.uri
            """)
    List<ViewStats> findViewStatsUniqueIp(@Param("start") LocalDateTime start,
                                          @Param("end") LocalDateTime end,
                                          @Param("uris") List<String> uris);
}
