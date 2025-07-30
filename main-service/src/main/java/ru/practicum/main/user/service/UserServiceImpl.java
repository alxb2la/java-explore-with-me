package ru.practicum.main.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.exception.UserEmailValidationException;
import ru.practicum.main.user.User;
import ru.practicum.main.user.UserMapper;
import ru.practicum.main.user.UserRepository;
import ru.practicum.main.user.dto.UserCreateDto;
import ru.practicum.main.user.dto.UserFullDto;

import java.util.Collection;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserFullDto addUser(UserCreateDto userCreateDto) {
        try {
            User user = userRepository.save(UserMapper.toUser(userCreateDto));
            return UserMapper.toUserFullDto(user);
        } catch (DataIntegrityViolationException e) {
            throw new UserEmailValidationException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public void removeUserById(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found by id: " + userId));
        userRepository.deleteById(userId);
    }

    @Override
    public Collection<UserFullDto> getUsersByParams(Integer from, Integer size, List<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            return userRepository.findByIdIn(ids).stream()
                    .map(UserMapper::toUserFullDto)
                    .toList();
        } else {
            return userRepository.findAll(PageRequest.of(from, size)).stream()
                    .map(UserMapper::toUserFullDto)
                    .toList();
        }
    }
}
