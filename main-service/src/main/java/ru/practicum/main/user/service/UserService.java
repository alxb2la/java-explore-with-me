package ru.practicum.main.user.service;

import ru.practicum.main.user.dto.UserCreateDto;
import ru.practicum.main.user.dto.UserFullDto;

import java.util.Collection;
import java.util.List;


public interface UserService {
    UserFullDto addUser(UserCreateDto user);

    void removeUserById(Long userId);

    Collection<UserFullDto> getUsersByParams(Integer from, Integer size, List<Long> ids);
}
