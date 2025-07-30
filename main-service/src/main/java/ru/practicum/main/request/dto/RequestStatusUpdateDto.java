package ru.practicum.main.request.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.practicum.main.request.model.RequestStatus;

import java.util.List;


@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@Builder
@ToString
public class RequestStatusUpdateDto {
    @NotNull
    private List<Long> requestIds;

    @NotNull
    private RequestStatus status;
}
