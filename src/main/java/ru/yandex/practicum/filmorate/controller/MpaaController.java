package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Mpaa;
import ru.yandex.practicum.filmorate.service.MpaaService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/mpa")
public class MpaaController {
    @Autowired
    private MpaaService service;

    @GetMapping
    public List<Mpaa> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Mpaa findById(@PathVariable int id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mpaa create(@Valid @RequestBody Mpaa mpa) {
        return service.create(mpa);
    }

    @PutMapping
    public Mpaa update(@Valid @RequestBody Mpaa mpa) {
        return service.update(mpa);
    }
}
