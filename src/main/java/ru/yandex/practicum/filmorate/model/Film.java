package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;


@Data
@AllArgsConstructor
@Builder
public class Film {
    final static int MAX_DESCRIPTION_LENGTH = 200;
    final static LocalDate CINEMA_BIRTHDAY = LocalDate.of(1895, 12, 28);

    private int id;

    private Set<Integer> likes;

    @NotBlank
    private String name;

    @Size(max=MAX_DESCRIPTION_LENGTH)
    private String description;

    private LocalDate releaseDate;

    @Positive
    private int duration;

    @NotNull
    private Mpaa mpa;

    private Set<Genre> genres;

    public boolean isReleaseDateCorrect() {
        return ! releaseDate.isBefore(CINEMA_BIRTHDAY);
    }
}
