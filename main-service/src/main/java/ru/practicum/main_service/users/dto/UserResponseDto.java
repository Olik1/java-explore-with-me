package ru.practicum.main_service.users.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {

    @NotBlank
    private String name;
    @NotBlank
    @Email
    private String email;


}
