package ru.yandex.practicum.filmorate.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;


@Data
@AllArgsConstructor
public class Film {
    final static int MAX_DESCRIPTION_LENGTH = 200;
    final static LocalDate CINEMA_BIRTHDAY = LocalDate.of(1895, 12, 28);

    private int id;
    @NotBlank
    private String name;
    @Size(max=MAX_DESCRIPTION_LENGTH)
    private String description;
    private LocalDate releaseDate;
    @Positive
    private int duration;

    public boolean isReleaseDateCorrect() {
        return ! releaseDate.isBefore(CINEMA_BIRTHDAY);
    }
}
