package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Data
@AllArgsConstructor
public class User {

    private int id;
    private final Set<Integer> friendsId = new HashSet<>();
    @Email
    private String email;
    @Pattern(regexp = "^[^ ]+$", message = "не должно содержать пробелов")
    private String login;
    private String name;
    @Past
    private LocalDate birthday;
}
