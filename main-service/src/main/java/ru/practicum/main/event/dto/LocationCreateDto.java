package ru.practicum.main.event.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationCreateDto {
    @NotNull
    @Min(value = -90)
    @Max(value = 90)
    private Float lat;

    @NotNull
    @Min(value = -180)
    @Max(value = 180)
    private Float lon;
}
