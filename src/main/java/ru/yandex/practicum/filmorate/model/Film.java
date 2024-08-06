package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.annotation.MinimumDate;

import java.time.LocalDate;
import java.util.Set;


@Data
@NoArgsConstructor
public class Film {
    private Set<Long> likes;
    private Set<Genre> genres;

    Long id;
    @NotBlank
    private String name;
    @Size(min = 1, max = 200)
    private String description;
    @MinimumDate
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;
    @PositiveOrZero
    @NotNull
    private Integer duration;
    @NotNull
    private Mpa mpa;
}
