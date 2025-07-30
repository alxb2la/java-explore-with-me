package ru.practicum.main.event;

import ru.practicum.main.category.Category;
import ru.practicum.main.category.CategoryMapper;
import ru.practicum.main.event.dto.EventCreateDto;
import ru.practicum.main.event.dto.EventFullDto;
import ru.practicum.main.event.dto.EventPrivateFullDto;
import ru.practicum.main.event.dto.EventShortDto;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.event.model.EventState;
import ru.practicum.main.event.model.Location;
import ru.practicum.main.user.User;
import ru.practicum.main.user.UserMapper;

import java.time.LocalDateTime;


public final class EventMapper {

    private EventMapper() {
        throw new UnsupportedOperationException();
    }

    public static Event toEvent(EventCreateDto eventCreateDto, User initiator, Category category) {
        Location location = Location.builder()
                .lat(eventCreateDto.getLocation().getLat())
                .lon(eventCreateDto.getLocation().getLon())
                .build();

        return Event.builder()
                .id(null)
                .annotation(eventCreateDto.getAnnotation())
                .category(category)
                .confirmedRequests(0L)
                .createdOn(LocalDateTime.now())
                .description(eventCreateDto.getDescription())
                .eventDate(eventCreateDto.getEventDate())
                .initiator(initiator)
                .location(location)
                .paid(eventCreateDto.getPaid())
                .participantLimit(eventCreateDto.getParticipantLimit())
                .publishedOn(null)
                .requestModeration(eventCreateDto.getRequestModeration())
                .state(EventState.PENDING)
                .title(eventCreateDto.getTitle())
                .build();
    }

    public static EventFullDto toEventFullDto(Event event, Long views) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryFullDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .location(event.getLocation())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(views)
                .build();
    }

    public static EventPrivateFullDto toEventPrivateFullDto(Event event, Long views) {
        return EventPrivateFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryFullDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .location(event.getLocation())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(views)
                .build();
    }

    public static EventShortDto toEventShortDto(Event event, Long views) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryFullDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(views)
                .build();
    }
}
