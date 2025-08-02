package ru.practicum.main.rating.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.event.EventRepository;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.exception.ConflictCommonException;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.rating.Rating;
import ru.practicum.main.rating.RatingMapper;
import ru.practicum.main.rating.RatingRepository;
import ru.practicum.main.rating.dto.RatingCreateDto;
import ru.practicum.main.rating.dto.RatingDto;
import ru.practicum.main.rating.dto.RatingEventDto;
import ru.practicum.main.user.User;
import ru.practicum.main.user.UserRepository;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RatingServiceImpl implements RatingService {
    private final RatingRepository ratingRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public RatingDto addRating(RatingCreateDto ratingCreateDto, Long evaluatorId, Long eventId) {
        User evaluator = userRepository.findById(evaluatorId)
                .orElseThrow(() -> new NotFoundException("User not found by id: " + evaluatorId));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found by id: " + eventId));
        if (ratingRepository.findByEvaluatorIdAndEventId(evaluatorId, eventId).isPresent()) {
            throw new ConflictCommonException("It is acceptable to evaluate any event only once.");
        }

        Rating rating = ratingRepository.save(RatingMapper.toRating(ratingCreateDto, evaluator, event));
        return RatingMapper.toRatingDto(rating);
    }

    @Override
    @Transactional
    public void removeRating(Long evaluatorId, Long eventId) {
        Rating rating = ratingRepository.findByEvaluatorIdAndEventId(evaluatorId, eventId)
                .orElseThrow(() -> new NotFoundException("Rating not found by EvaluatorId and EventId"));

        ratingRepository.delete(rating);
    }

    @Override
    public RatingDto getRating(Long evaluatorId, Long eventId) {
        User evaluator = userRepository.findById(evaluatorId)
                .orElseThrow(() -> new NotFoundException("User not found by id: " + evaluatorId));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found by id: " + eventId));

        Rating rating = ratingRepository.findByEvaluatorIdAndEventId(evaluatorId, eventId)
                .orElseThrow(() -> new NotFoundException("Rating not found by EvaluatorId and EventId"));
        return RatingMapper.toRatingDto(rating);
    }

    @Override
    public RatingEventDto getRatingOfEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found by id: " + eventId));

        return ratingRepository.findRatingOfEvent(eventId);
    }

    @Override
    public List<RatingEventDto> getTopRatingsOfEvents(Integer from, Integer size) {
        List<RatingEventDto> ratings = ratingRepository.findTopRatingsOfEvents(PageRequest.of(from, size))
                .getContent();
        return ratings.stream()
                .sorted((r1, r2) -> r2.getLike().compareTo(r1.getLike()))
                .toList();
    }
}
