package ru.practicum.main.user.dto;

import lombok.*;


@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@Builder
@ToString
public class UserFullDto {
    private Long id;
    private String name;
    private String email;
}
