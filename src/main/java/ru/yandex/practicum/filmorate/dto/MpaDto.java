package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.model.Mpa;

@EqualsAndHashCode(callSuper = true)
@Data
public class MpaDto extends Mpa {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;
    private String name;
}
