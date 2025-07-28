package ru.practicum.main.event.service;

import ru.practicum.main.event.dto.*;

import java.util.Collection;

public interface EventService {
    EventFullDto addEvent(EventCreateDto eventCreateDto, Long userId);

    EventFullDto updateEventByInitiator(EventUpdateUserReqDto eventDto, Long userId, Long eventId);

    Collection<EventShortDto> getEventsByInitiatorId(Long userId, Integer from, Integer size);

    EventPrivateFullDto getEventByInitiatorIdAndEventId(Long userId, Long eventId);

    EventFullDto updateEventByAdmin(EventUpdateAdminReqDto eventDto, Long eventId);

    Collection<EventFullDto> getEventsByAdmin(EventGetParamsDto eventParamsDto);

    Collection<EventShortDto> getPublicEvents(EventGetPublicParamsDto eventParamsDto);

    EventFullDto getPublicEventById(Long id);
}
