package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.MinimumDate;

import java.time.LocalDate;

@Builder
@Data
public class Film {
    Long id;
    @NotBlank
    String name;
    @Size(min = 1, max = 200)
    String description;
    @MinimumDate
    LocalDate releaseDate;
    @PositiveOrZero
    @NotNull
    Integer duration;
}
