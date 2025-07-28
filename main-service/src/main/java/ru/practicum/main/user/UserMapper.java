package ru.practicum.main.user;

import ru.practicum.main.user.dto.UserCreateDto;
import ru.practicum.main.user.dto.UserFullDto;
import ru.practicum.main.user.dto.UserShortDto;


public final class UserMapper {

    private UserMapper() {
        throw new UnsupportedOperationException();
    }

    public static UserFullDto toUserFullDto(User user) {
        return UserFullDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static UserShortDto toUserShortDto(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public static User toUser(UserCreateDto userCreateDto) {
        return User.builder()
                .id(null)
                .name(userCreateDto.getName())
                .email(userCreateDto.getEmail())
                .build();
    }
}
