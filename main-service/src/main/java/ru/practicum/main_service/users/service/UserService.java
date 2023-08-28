package ru.practicum.main_service.users.service;

import ru.practicum.main_service.users.dto.NewUserRequestDto;
import ru.practicum.main_service.users.dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> getUsers(List<Long> ids, Integer from, Integer size);

    UserDto createUser(NewUserRequestDto userRequestDto);

    void deleteUser(Long userId);
}
