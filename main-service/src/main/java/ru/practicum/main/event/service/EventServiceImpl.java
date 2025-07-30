package ru.practicum.main.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.category.Category;
import ru.practicum.main.category.CategoryRepository;
import ru.practicum.main.event.EventMapper;
import ru.practicum.main.event.EventRepository;
import ru.practicum.main.event.dto.*;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.event.model.EventState;
import ru.practicum.main.event.model.Location;
import ru.practicum.main.exception.ConflictCommonException;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.exception.ValidationException;
import ru.practicum.main.user.User;
import ru.practicum.main.user.UserRepository;
import ru.practicum.main.util.ViewStatsConverter;
import ru.practicum.stats.client.StatClient;
import ru.practicum.stats.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final StatClient statClient;

    @Override
    @Transactional
    public EventFullDto addEvent(EventCreateDto eventCreateDto, Long userId) {
        User initiator = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found by id: " + userId));
        Category category = categoryRepository.findById(eventCreateDto.getCategory())
                .orElseThrow(() -> new NotFoundException("Category not found by id: " + eventCreateDto.getCategory()));
        if (eventCreateDto.getPaid() == null) {
            eventCreateDto.setPaid(false);
        }
        if (eventCreateDto.getParticipantLimit() == null) {
            eventCreateDto.setParticipantLimit(0);
        }
        if (eventCreateDto.getRequestModeration() == null) {
            eventCreateDto.setRequestModeration(true);
        }

        Event event = eventRepository.save(EventMapper.toEvent(eventCreateDto, initiator, category));
        return EventMapper.toEventFullDto(event, 0L);
    }


    @Override
    @Transactional
    public EventFullDto updateEventByInitiator(EventUpdateUserReqDto eventDto, Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found by id: " + eventId));
        if (!userId.equals(event.getInitiator().getId())) {
            //throw new ValidationException("User with id: " + userId + " isn't a initiator of Event with id: " + eventId);
            throw new ConflictCommonException("User with id: " + userId + " isn't a initiator of Event with id: " + eventId);
        }
        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictCommonException("Only pending or canceled events can be update");
        }

        if (eventDto.getAnnotation() != null) {
            event.setAnnotation(eventDto.getAnnotation());
        }
        if (eventDto.getCategory() != null) {
            Category category = categoryRepository.findById(eventDto.getCategory())
                    .orElseThrow(() -> new NotFoundException("Category not found by id: " + eventDto.getCategory()));
            event.setCategory(category);
        }
        if (eventDto.getDescription() != null) {
            event.setDescription(eventDto.getDescription());
        }
        if (eventDto.getEventDate() != null) {
            if (!eventDto.getEventDate().isAfter(LocalDateTime.now().plusHours(2))) {
                throw new ValidationException("The date and time for which the event is scheduled " +
                        "cannot be earlier than two hours from the current moment");
            }
            event.setEventDate(eventDto.getEventDate());
        }
        if (eventDto.getLocation() != null) {
            Location location = Location.builder()
                    .lat(eventDto.getLocation().getLat())
                    .lon(eventDto.getLocation().getLon())
                    .build();
            event.setLocation(location);
        }
        if (eventDto.getPaid() != null) {
            event.setPaid(eventDto.getPaid());
        }
        if (eventDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventDto.getParticipantLimit());
        }
        if (eventDto.getRequestModeration() != null) {
            event.setRequestModeration(eventDto.getRequestModeration());
        }
        if (eventDto.getStateAction() != null) {
            switch (eventDto.getStateAction()) {
                case EventStateAction.SEND_TO_REVIEW -> event.setState(EventState.PENDING);
                case EventStateAction.CANCEL_REVIEW -> event.setState(EventState.CANCELED);
            }
        }
        if (eventDto.getTitle() != null) {
            event.setTitle(eventDto.getTitle());
        }

        eventRepository.save(event);

        // Get views
        List<ViewStatsDto> viewStats = statClient.getViewStats(
                event.getCreatedOn(),
                LocalDateTime.now(),
                List.of("/events/" + eventId),
                true
        );
        if ((viewStats == null) || (viewStats.isEmpty())) {
            return EventMapper.toEventFullDto(event, 0L);
        }
        return EventMapper.toEventFullDto(event, viewStats.get(0).getHits());
    }

    @Override
    public Collection<EventShortDto> getEventsByInitiatorId(Long userId, Integer from, Integer size) {
        User initiator = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found by id: " + userId));
        List<Event> events = eventRepository.findByInitiatorId(userId,
                PageRequest.of(from, size, Sort.by("createdOn").ascending())).getContent();

        // Get views
        List<String> uris = events.stream()
                .map(e -> "/events/" + e.getId())
                .toList();
        List<ViewStatsDto> viewStats = statClient.getViewStats(
                events.getFirst().getCreatedOn(),
                LocalDateTime.now(),
                uris,
                true
        );
        Map<Long, Long> views = ViewStatsConverter.mapViewStatsDtos(viewStats);

        return events.stream()
                .map(e -> EventMapper.toEventShortDto(e, views.getOrDefault(e.getId(), 0L)))
                .toList();
    }

    @Override
    public EventPrivateFullDto getEventByInitiatorIdAndEventId(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found by id: " + eventId));
        if (!userId.equals(event.getInitiator().getId())) {
            //throw new ValidationException("User with id: " + userId + " isn't a initiator of Event with id: " + eventId);
            throw new ConflictCommonException("User with id: " + userId + " isn't a initiator of Event with id: " + eventId);
        }

        // Get views
        List<ViewStatsDto> viewStats = statClient.getViewStats(
                event.getCreatedOn(),
                LocalDateTime.now(),
                List.of("/events/" + eventId),
                true
        );
        if ((viewStats == null) || (viewStats.isEmpty())) {
            return EventMapper.toEventPrivateFullDto(event, 0L);
        }
        return EventMapper.toEventPrivateFullDto(event, viewStats.get(0).getHits());
    }

    @Override
    @Transactional
    public EventFullDto updateEventByAdmin(EventUpdateAdminReqDto eventDto, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found by id: " + eventId));

        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new ValidationException("The start date of the event to be modified " +
                    "must be no earlier than one hour from the publication date.");
        }

        if (eventDto.getStateAction() != null) {
            if (eventDto.getStateAction().equals(EventStateAction.PUBLISH_EVENT)) {
                if (!event.getState().equals(EventState.PENDING)) {
                    throw new ConflictCommonException("An event can only be published " +
                            "if it is in the waiting state for publication");
                }
                event.setPublishedOn(LocalDateTime.now());
                event.setState(EventState.PUBLISHED);
            } else if (eventDto.getStateAction().equals(EventStateAction.REJECT_EVENT)) {
                if (event.getState().equals(EventState.PUBLISHED)) {
                    throw new ConflictCommonException("An event can only be rejected " +
                            "if it has not been published yet.");
                }
                event.setState(EventState.CANCELED);
            } else {
                throw new ValidationException("Unsupported EventState type");
            }
        }

        if (eventDto.getAnnotation() != null) {
            event.setAnnotation(eventDto.getAnnotation());
        }
        if (eventDto.getCategory() != null) {
            Category category = categoryRepository.findById(eventDto.getCategory())
                    .orElseThrow(() -> new NotFoundException("Category not found by id: " + eventDto.getCategory()));
            event.setCategory(category);
        }
        if (eventDto.getDescription() != null) {
            event.setDescription(eventDto.getDescription());
        }
        if (eventDto.getEventDate() != null) {
            if (eventDto.getEventDate().isBefore(LocalDateTime.now())) {
                throw new ValidationException("Changing the date of an event to one that has already occurred");
            }
            event.setEventDate(eventDto.getEventDate());
        }
        if (eventDto.getLocation() != null) {
            Location location = Location.builder()
                    .lat(eventDto.getLocation().getLat())
                    .lon(eventDto.getLocation().getLon())
                    .build();
            event.setLocation(location);
        }
        if (eventDto.getPaid() != null) {
            event.setPaid(eventDto.getPaid());
        }
        if (eventDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventDto.getParticipantLimit());
        }
        if (eventDto.getRequestModeration() != null) {
            event.setRequestModeration(eventDto.getRequestModeration());
        }
        if (eventDto.getTitle() != null) {
            event.setTitle(eventDto.getTitle());
        }

        eventRepository.save(event);

        // Get views
        List<ViewStatsDto> viewStats = statClient.getViewStats(
                event.getCreatedOn(),
                LocalDateTime.now(),
                List.of("/events/" + eventId),
                true
        );
        if ((viewStats == null) || (viewStats.isEmpty())) {
            return EventMapper.toEventFullDto(event, 0L);
        }
        return EventMapper.toEventFullDto(event, viewStats.get(0).getHits());
    }

    @Override
    public Collection<EventFullDto> getEventsByAdmin(EventGetParamsDto eventParamsDto) {
        List<EventState> states = null;
        if (eventParamsDto.getStates() != null && !eventParamsDto.getStates().isEmpty()) {
            states = eventParamsDto.getStates().stream()
                    .map(String::toUpperCase)
                    .map(EventState::valueOf)
                    .toList();
        }
        if ((eventParamsDto.getUsers() == null) || (eventParamsDto.getUsers().isEmpty())) {
            eventParamsDto.setUsers(null);
        }
        if ((eventParamsDto.getCategories() == null) || (eventParamsDto.getCategories().isEmpty())) {
            eventParamsDto.setCategories(null);
        }

        List<Event> events = eventRepository.findByAdminParams(
                eventParamsDto.getUsers(),
                states,
                eventParamsDto.getCategories(),
                eventParamsDto.getRangeStart(),
                eventParamsDto.getRangeEnd(),
                eventParamsDto.getFrom(),
                eventParamsDto.getSize()
        );

        // Get views
        List<String> uris = events.stream()
                .map(e -> "/events/" + e.getId())
                .toList();
        List<ViewStatsDto> viewStats = statClient.getViewStats(
                events.getFirst().getCreatedOn(),
                LocalDateTime.now(),
                uris,
                true
        );
        Map<Long, Long> views = ViewStatsConverter.mapViewStatsDtos(viewStats);

        return events.stream()
                .map(e -> EventMapper.toEventFullDto(e, views.getOrDefault(e.getId(), 0L)))
                .toList();
    }

    @Override
    public Collection<EventShortDto> getPublicEvents(EventGetPublicParamsDto eventParamsDto) {
        if ((eventParamsDto.getRangeStart() != null) && (eventParamsDto.getRangeEnd() != null)
                && (eventParamsDto.getRangeStart().isAfter(eventParamsDto.getRangeEnd()))) {
            throw new ValidationException("Start date must be before end date");
        }
        if ((eventParamsDto.getCategories() != null)
                && (eventParamsDto.getCategories().stream().anyMatch(c -> c < 0))) {
            throw new ValidationException("The value of the category id cannot be negative");
        }
        if ((eventParamsDto.getCategories() == null) || (eventParamsDto.getCategories().isEmpty())) {
            eventParamsDto.setCategories(null);
        }

        List<Event> events;
        if (eventParamsDto.getText() == null || eventParamsDto.getText().isBlank()) {
            events = eventRepository.findByPublicParamsNoText(
                    eventParamsDto.getCategories(),
                    eventParamsDto.getPaid(),
                    eventParamsDto.getRangeStart(),
                    eventParamsDto.getRangeEnd(),
                    eventParamsDto.getFrom(),
                    eventParamsDto.getSize()
            );
        } else {
            events = eventRepository.findByPublicParams(
                    eventParamsDto.getText(),
                    eventParamsDto.getCategories(),
                    eventParamsDto.getPaid(),
                    eventParamsDto.getRangeStart(),
                    eventParamsDto.getRangeEnd(),
                    eventParamsDto.getFrom(),
                    eventParamsDto.getSize()
            );
        }



        // Get views
        List<String> uris = events.stream()
                .map(e -> "/events/" + e.getId())
                .toList();
        List<ViewStatsDto> viewStats = statClient.getViewStats(
                events.getFirst().getCreatedOn(),
                LocalDateTime.now(),
                uris,
                true
        );
        Map<Long, Long> views = ViewStatsConverter.mapViewStatsDtos(viewStats);
        List<EventShortDto> eventShortDtos = events.stream()
                .map(e -> EventMapper.toEventShortDto(e, views.getOrDefault(e.getId(), 0L)))
                .toList();

        // Sort by param
        if (eventParamsDto.getSort() != null) {
            if (eventParamsDto.getSort().equals(EventSortingType.EVENT_DATE)) {
                eventShortDtos = eventShortDtos.stream()
                        .sorted((e1, e2) -> e1.getEventDate().compareTo(e2.getEventDate()))
                        .toList();
            } else if (eventParamsDto.getSort().equals(EventSortingType.VIEWS)) {
                eventShortDtos = eventShortDtos.stream()
                        .sorted((e1, e2) -> e1.getViews().compareTo(e2.getViews()))
                        .toList();
            }
        }

        return eventShortDtos;
    }

    @Override
    public EventFullDto getPublicEventById(Long eventId) {
        Event event = eventRepository.findByIdAndState(eventId, EventState.PUBLISHED)
                .orElseThrow(() -> new NotFoundException("Published Event not found by id: " + eventId));

        // Get views
        List<ViewStatsDto> viewStats = statClient.getViewStats(
                event.getCreatedOn(),
                LocalDateTime.now(),
                List.of("/events/" + eventId),
                true
        );
        if ((viewStats == null) || (viewStats.isEmpty())) {
            return EventMapper.toEventFullDto(event, 0L);
        }
        return EventMapper.toEventFullDto(event, viewStats.get(0).getHits());
    }
}
