package ru.practicum.main.category.dto;

import lombok.*;


@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@Builder
@ToString
public class CategoryFullDto {
    private Long id;
    private String name;
}
