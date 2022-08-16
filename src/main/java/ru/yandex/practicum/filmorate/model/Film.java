package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Data
@AllArgsConstructor
public class Film {
    final static int MAX_DESCRIPTION_LENGTH = 200;
    final static LocalDate CINEMA_BIRTHDAY = LocalDate.of(1895, 12, 28);

    private int id;
    private final Set<Integer> likes = new HashSet<>();
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
