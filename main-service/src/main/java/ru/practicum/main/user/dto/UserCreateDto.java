package ru.practicum.main.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;


@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@Builder
@ToString
public class UserCreateDto {
    @NotBlank
    @Size(min = 2, max = 250)
    private String name;

    @NotNull
    @Email
    @Size(min = 6, max = 254)
    private String email;
}
