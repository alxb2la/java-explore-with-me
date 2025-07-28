package ru.practicum.main.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.event.model.EventState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findByInitiatorId(Long userId, Pageable pageable);

    Optional<Event> findFistByCategoryId(Long categoryId);

    List<Event> findByIdInOrderByIdAsc(List<Long> ids);

    Optional<Event> findByIdAndInitiatorId(Long eventId, Long userId);

    Optional<Event> findByIdAndState(Long id, EventState state);

    @Query("""
             SELECT e
             FROM Event e
             WHERE ((:states IS NULL) OR (e.state IN :states))
               AND ((:categories IS NULL) OR (e.category.id IN :categories))
               AND ((:users IS NULL) OR (e.initiator.id IN :users))
               AND (e.eventDate >= COALESCE(:rangeStart, e.eventDate))
               AND (e.eventDate <= COALESCE(:rangeEnd, e.eventDate))
             ORDER BY e.createdOn ASC
             LIMIT :size
             OFFSET :from
            """)
    List<Event> findByAdminParams(@Param("users") List<Long> users,
                                  @Param("states") List<EventState> states,
                                  @Param("categories") List<Long> categories,
                                  @Param("rangeStart") LocalDateTime rangeStart,
                                  @Param("rangeEnd") LocalDateTime rangeEnd,
                                  @Param("from") Integer from,
                                  @Param("size") Integer size);


    @Query("""
            SELECT e
            FROM Event e
            WHERE e.state = 'PUBLISHED'
              AND ((:text IS NULL)
                OR (LOWER(e.description) LIKE LOWER(CONCAT('%', :text, '%')))
                OR (LOWER(e.annotation) LIKE LOWER(CONCAT('%', :text, '%'))))
              AND ((:categories IS NULL) OR (e.category.id IN :categories))
              AND ((:paid IS NULL) OR (e.paid = :paid))
              AND (e.eventDate >= COALESCE(:rangeStart, e.eventDate))
              AND (e.eventDate <= COALESCE(:rangeEnd, e.eventDate))
            ORDER BY e.createdOn ASC
            LIMIT :size
            OFFSET :from
            """)
    List<Event> findByPublicParams(@Param("text") String text,
                                   @Param("categories") List<Long> categories,
                                   @Param("paid") Boolean paid,
                                   @Param("rangeStart") LocalDateTime rangeStart,
                                   @Param("rangeEnd") LocalDateTime rangeEnd,
                                   @Param("from") Integer from,
                                   @Param("size") Integer size);
}
