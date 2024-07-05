package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class User {
    Long id;
    @Email
    String email;
    @NotBlank
    @Pattern(regexp = "^\\S*$")
    String login;
    String name;
    @NotNull
    @Past(message = "date of birth must be less than today")
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate birthday;
}
