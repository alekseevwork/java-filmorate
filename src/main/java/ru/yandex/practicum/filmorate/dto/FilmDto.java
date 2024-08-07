package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class FilmDto extends Film {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long id;
    private String name;
    private String description;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate releaseDate;
    private Integer duration;
    private Mpa mpa;
}
