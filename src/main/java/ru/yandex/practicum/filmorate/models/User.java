package ru.yandex.practicum.filmorate.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;


@Data
@AllArgsConstructor
public class User {

    private int id;
    @Email
    private String email;
    @Pattern(regexp = "^[^ ]+$")
    private String login;
    private String name;
    @Past
    private LocalDate birthday;
}
