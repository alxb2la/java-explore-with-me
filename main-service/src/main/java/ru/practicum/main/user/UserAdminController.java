package ru.practicum.main.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.user.dto.UserCreateDto;
import ru.practicum.main.user.dto.UserFullDto;
import ru.practicum.main.user.service.UserService;

import java.util.Collection;
import java.util.List;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(
        path = "/admin/users",
        produces = "application/json"
)
public class UserAdminController {
    private final UserService userService;

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public UserFullDto addUser(@RequestBody @Valid UserCreateDto user) {
        log.info("Запрос на добавление User: {}", user);
        UserFullDto userFullDto = userService.addUser(user);
        log.info("Успешно добавлен User: {}", userFullDto);
        return userFullDto;
    }

    @DeleteMapping(path = "/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeUserById(@PathVariable @NotNull Long userId) {
        log.info("Запрос на удаление User по id: {}", userId);
        userService.removeUserById(userId);
        log.info("Успешно удален User с id: {}", userId);
    }

    @GetMapping
    public Collection<UserFullDto> getUsersByParams(
            @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(name = "size", defaultValue = "10") @Positive Integer size,
            @RequestParam(name = "ids", required = false) List<Long> ids) {
        log.info("Запрос на получение списка Users: from {}, size {}, ids {}", from, size, ids);
        Collection<UserFullDto> users = userService.getUsersByParams(from, size, ids);
        log.info("Успешно сформирован список Users размером {}", users.size());
        return users;
    }
}
