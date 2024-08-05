package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class FilmDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long id;
    private String name;
    private String description;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate releaseDate;
}
