package ru.practicum.main.request;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.request.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {

    Optional<Request> findByRequesterIdAndEventId(Long requesterId, Long eventId);

    Optional<Request> findByIdAndRequesterId(Long requestId, Long requesterId);

    @EntityGraph(attributePaths = {"event", "requester"}, type = EntityGraph.EntityGraphType.LOAD)
    List<Request> findAllByRequesterId(Long userId);

    @EntityGraph(attributePaths = {"event", "requester"}, type = EntityGraph.EntityGraphType.LOAD)
    List<Request> findAllByIdInAndEventId(List<Long> ids, Long eventId);

    @EntityGraph(attributePaths = {"event", "requester"}, type = EntityGraph.EntityGraphType.LOAD)
    List<Request> findAllByEventId(Long eventId);
}
