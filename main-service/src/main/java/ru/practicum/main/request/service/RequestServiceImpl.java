package ru.practicum.main.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.event.EventRepository;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.event.model.EventState;
import ru.practicum.main.exception.ConflictCommonException;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.exception.ValidationException;
import ru.practicum.main.request.RequestMapper;
import ru.practicum.main.request.RequestRepository;
import ru.practicum.main.request.dto.RequestDto;
import ru.practicum.main.request.dto.RequestStatusResultDto;
import ru.practicum.main.request.dto.RequestStatusUpdateDto;
import ru.practicum.main.request.model.Request;
import ru.practicum.main.request.model.RequestStatus;
import ru.practicum.main.user.User;
import ru.practicum.main.user.UserRepository;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public RequestDto addRequest(Long userId, Long eventId) {
        User requester = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found by id: " + userId));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found by id: " + eventId));
        if (requestRepository.findByRequesterIdAndEventId(userId, eventId).isPresent()) {
            throw new ConflictCommonException("Attempt to add a repeat request");
        }
        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictCommonException("The initiator of the event " +
                    "cannot add a request to participate in his event");
        }
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictCommonException("Attempt to apply for an unpublished event");
        }
        if ((event.getParticipantLimit() > 0) && (event.getConfirmedRequests() >= event.getParticipantLimit())) {
            throw new ConflictCommonException("The limit of participation requests has been reached");
        }

        RequestStatus requestStatus;
        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            requestStatus = RequestStatus.CONFIRMED;
            if (event.getConfirmedRequests() == null) {
                event.setConfirmedRequests(1L);
            } else {
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            }
            eventRepository.save(event);
        } else {
            requestStatus = RequestStatus.PENDING;
        }

        Request request = requestRepository.save(RequestMapper.toRequest(requester, event, requestStatus));
        return RequestMapper.toRequestDto(request);
    }

    @Override
    @Transactional
    public RequestDto updateRequestStatusToCanceledByUser(Long userId, Long requestId) {
        User requester = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found by id: " + userId));
        Request request = requestRepository.findByIdAndRequesterId(requestId, userId)
                .orElseThrow(() -> new NotFoundException("Request not found by id: " + requestId + " and" +
                        " by Requester id: " + userId));

        if (request.getStatus().equals(RequestStatus.CONFIRMED)) {
            Event event = request.getEvent();
            event.setConfirmedRequests(event.getConfirmedRequests() - 1);
            eventRepository.save(event);
        }
        request.setStatus(RequestStatus.CANCELED);

        requestRepository.save(request);
        return RequestMapper.toRequestDto(request);
    }

    @Override
    public List<RequestDto> getRequestsByUserId(Long userId) {
        User requester = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found by id: " + userId));
        List<Request> requests = requestRepository.findAllByRequesterId(userId);
        return requests.stream()
                .map(RequestMapper::toRequestDto)
                .toList();
    }

    @Override
    @Transactional
    public RequestStatusResultDto updateRequestsStatus(RequestStatusUpdateDto updateDto, Long userId, Long eventId) {
        User requester = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found by id: " + userId));
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Event not found by id: " + eventId + " and" +
                        " by Initiator id: " + userId));
        if (updateDto.getRequestIds().isEmpty()) {
            throw new ValidationException("Еhe statuses of applications for participation in the event " +
                    "could not be changed. The list of IDs is empty");
        }
        if ((event.getConfirmedRequests() != null) && (event.getConfirmedRequests() >= event.getParticipantLimit())) {
            throw new ConflictCommonException("The limit of participation requests has been reached");
        }

        List<Request> requests = requestRepository.findAllByIdInAndEventId(updateDto.getRequestIds(), eventId);

        if (event.getParticipantLimit() == 0 || event.getRequestModeration().equals(false)) {
            List<RequestDto> confirmedRequests = requests.stream()
                    .map(RequestMapper::toRequestDto)
                    .toList();
            return RequestStatusResultDto.builder()
                    .confirmedRequests(confirmedRequests)
                    .rejectedRequests(List.of())
                    .build();
        }

        List<RequestDto> confirmedRequests = List.of();
        List<RequestDto> rejectedRequests = List.of();
        if (updateDto.getStatus().equals(RequestStatus.CONFIRMED)) {
            for (Request request : requests) {
                if ((event.getConfirmedRequests() != null)
                        && (event.getConfirmedRequests() >= event.getParticipantLimit())) {
                    throw new ConflictCommonException("The limit of participation requests has been reached");
                }
                event.setConfirmedRequests(event.getConfirmedRequests() == null ? 1 : event.getConfirmedRequests() + 1);
                request.setStatus(RequestStatus.CONFIRMED);
            }
            confirmedRequests = requests.stream()
                    .map(RequestMapper::toRequestDto)
                    .toList();
            eventRepository.save(event);
        } else if (updateDto.getStatus().equals(RequestStatus.REJECTED)) {
            for (Request request : requests) {
                request.setStatus(RequestStatus.REJECTED);
            }
            rejectedRequests = requests.stream()
                    .map(RequestMapper::toRequestDto)
                    .toList();
        } else {
            throw new ValidationException("Unsupported Request Status");
        }

        requestRepository.saveAllAndFlush(requests);

        return RequestStatusResultDto.builder()
                .confirmedRequests(confirmedRequests)
                .rejectedRequests(rejectedRequests)
                .build();
    }

    @Override
    public List<RequestDto> getRequestsByUserIdAndEventId(Long userId, Long eventId) {
        User initiator = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found by id: " + userId));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found by id: " + eventId));
        if (!event.getInitiator().getId().equals(userId)) {
            throw new ConflictCommonException("The user is not the initiator of the event");
        }

        List<Request> requests = requestRepository.findAllByEventId(eventId);
        return requests.stream()
                .map(RequestMapper::toRequestDto)
                .toList();
    }
}
