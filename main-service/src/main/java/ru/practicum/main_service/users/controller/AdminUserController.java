package ru.practicum.main_service.users.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.users.dto.UserRequestDto;
import ru.practicum.main_service.users.dto.UserResponseDto;
import ru.practicum.main_service.users.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class AdminUserController {
    public final UserService userService;

    @GetMapping
    public List<UserResponseDto> getUsers(@RequestParam(required = false) List<Long> ids,
                                          @RequestParam(required = false, defaultValue = "0")
                               @PositiveOrZero Integer from,
                                          @RequestParam(required = false, defaultValue = "10")
                               @PositiveOrZero Integer size) {
        return userService.getUsers(ids, from, size);
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserRequestDto createUser(@RequestBody @Valid UserRequestDto userRequestDto) {
        return userService.createUser(userRequestDto);
    }
    @DeleteMapping
    @RequestMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }

}
