package ru.practicum.main_service.users.service;

import ru.practicum.main_service.users.dto.UserRequestDto;
import ru.practicum.main_service.users.dto.UserResponseDto;

import java.util.List;

public interface UserService {

    List<UserResponseDto> getUsers(List<Long> ids, Integer from, Integer size);
    UserRequestDto createUser (UserRequestDto userRequestDto);
    void deleteUser(Long userId);
}
