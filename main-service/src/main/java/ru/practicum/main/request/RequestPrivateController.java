package ru.practicum.main.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.request.dto.RequestDto;
import ru.practicum.main.request.dto.RequestStatusResultDto;
import ru.practicum.main.request.dto.RequestStatusUpdateDto;
import ru.practicum.main.request.service.RequestService;

import java.util.List;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(
        path = "/users",
        produces = "application/json"
)
public class RequestPrivateController {
    private final RequestService requestService;

    @PostMapping(path = "/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto addRequest(@PathVariable @NotNull Long userId,
                                 @RequestParam(name = "eventId") @Positive Long eventId) {
        log.info("Запрос на добавление Request: userId {}, eventId {}", userId, eventId);
        RequestDto requestDto = requestService.addRequest(userId, eventId);
        log.info("Успешно добавлен Request: {}", requestDto);
        return requestDto;
    }

    @PatchMapping(path = "/{userId}/requests/{requestId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public RequestDto updateRequestStatusToCanceledByUser(@PathVariable @NotNull Long userId,
                                                          @PathVariable @NotNull Long requestId) {
        log.info("Запрос на обновление Request status на CANCELED: User id {}, Request id {}", userId, requestId);
        RequestDto requestDto = requestService.updateRequestStatusToCanceledByUser(userId, requestId);
        log.info("Успешно обновлен Request status на CANCELED: {}", requestDto);
        return requestDto;
    }

    @GetMapping(path = "/{userId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<RequestDto> getRequestsByUserId(@PathVariable @NotNull Long userId) {
        log.info("Запрос на получение списка Requests: User id {}", userId);
        List<RequestDto> requests = requestService.getRequestsByUserId(userId);
        log.info("Успешно получен список Requests размером: {}", requests.size());
        return requests;
    }

    @PatchMapping(path = "/{userId}/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public RequestStatusResultDto updateRequestsStatus(@PathVariable @NotNull Long userId,
                                                       @PathVariable @NotNull Long eventId,
                                                       @RequestBody @Valid RequestStatusUpdateDto updateDto) {
        log.info("Запрос на обновление Requests statuses: User id {}, Event id {}", userId, eventId);
        RequestStatusResultDto resultDto = requestService.updateRequestsStatus(updateDto, userId, eventId);
        log.info("Успешно обновлены Requests statuses: {}", resultDto);
        return resultDto;
    }


    @GetMapping(path = "/{userId}/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<RequestDto> getRequestsByUserIdAndEventId(@PathVariable @NotNull Long userId,
                                                          @PathVariable @NotNull Long eventId) {
        log.info("Запрос на получение списка Requests: User id {}, Event id {}", userId, eventId);
        List<RequestDto> requests = requestService.getRequestsByUserIdAndEventId(userId, eventId);
        log.info("Успешно получен список Requests события размером: {}", requests.size());
        return requests;
    }
}
