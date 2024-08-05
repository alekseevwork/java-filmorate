package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class NewUserRequest {
    @Email
    private String email;
    @NotBlank
    @Pattern(regexp = "^\\S*$")
    private String login;
    private String name;
    @Past(message = "date of birth must be less than today")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
}
