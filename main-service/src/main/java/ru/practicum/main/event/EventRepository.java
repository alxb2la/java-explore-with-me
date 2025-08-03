package ru.practicum.main.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.event.model.EventState;

import java.util.List;
import java.util.Optional;


public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {

    Page<Event> findByInitiatorId(Long userId, Pageable pageable);

    Optional<Event> findFistByCategoryId(Long categoryId);

    List<Event> findByIdInOrderByIdAsc(List<Long> ids);

    Optional<Event> findByIdAndInitiatorId(Long eventId, Long userId);

    Optional<Event> findByIdAndState(Long id, EventState state);
}
