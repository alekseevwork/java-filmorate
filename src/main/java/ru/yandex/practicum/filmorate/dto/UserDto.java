package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserDto extends User {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long id;
    private String email;
    private String login;
    private String name;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate birthday;
}
