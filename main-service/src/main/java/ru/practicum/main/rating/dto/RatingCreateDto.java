package ru.practicum.main.rating.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@Builder
@ToString
public class RatingCreateDto {
    @NotNull
    private Boolean rated;
}
