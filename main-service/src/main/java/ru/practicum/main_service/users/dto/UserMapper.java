package ru.practicum.main_service.users.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.main_service.users.model.User;

@UtilityClass
public class UserMapper {
    public static UserResponseDto toUserResponseDto(User user) {
        return UserResponseDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    public static User toUser (UserRequestDto userRequestDto) {
        return User.builder()
                .email(userRequestDto.getEmail())
                .name(userRequestDto.getName())
                .build();
    }
    public static UserRequestDto toUserRequestDto (User user) {
        return UserRequestDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }
}
