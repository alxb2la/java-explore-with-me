package ru.practicum.main.rating;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.main.rating.dto.RatingEventDto;

import java.util.Optional;


public interface RatingRepository extends JpaRepository<Rating, Long> {

    Optional<Rating> findByEvaluatorIdAndEventId(Long evaluatorId, Long eventId);

    @Query("""
             SELECT new ru.practicum.main.rating.dto.RatingEventDto(
                 r.event.id,
                 SUM(CASE WHEN r.rated = true THEN 1 ELSE 0 END) AS like,
                 SUM(CASE WHEN r.rated = false THEN 1 ELSE 0 END) AS dislike
             )
             FROM Rating r
             WHERE r.event.id = :eventId
             GROUP BY r.event.id
            """)
    RatingEventDto findRatingOfEvent(@Param("eventId") Long eventId);

    @Query("""
             SELECT new ru.practicum.main.rating.dto.RatingEventDto(
                 r.event.id,
                 SUM(CASE WHEN r.rated = true THEN 1 ELSE 0 END) AS like,
                 SUM(CASE WHEN r.rated = false THEN 1 ELSE 0 END) AS dislike
             )
             FROM Rating r
             GROUP BY r.event.id
            """)
    Page<RatingEventDto> findTopRatingsOfEvents(Pageable pageable);
}
