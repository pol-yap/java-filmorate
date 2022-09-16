package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Mpaa;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MpaaService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
public class MpaaController {
    private final MpaaService service;

    @Autowired
    public MpaaController(MpaaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Mpaa> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Mpaa findById(@PathVariable int id) {
        return service.findById(id);
    }
}
