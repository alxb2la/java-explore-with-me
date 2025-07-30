package ru.practicum.main.compilation.dto;

import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;


@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@Builder
@ToString
public class CompilationUpdateDto {
    @Size(min = 1, max = 50)
    private String title;

    private Boolean pinned;

    private List<Long> events;
}
