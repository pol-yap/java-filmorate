package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.SimpleEntity;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@RestController
@RequestMapping("/genres")
public class GenreController {
    @Autowired
    private GenreService service;

    @GetMapping
    public List<SimpleEntity> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public SimpleEntity findById(@PathVariable int id) {
        return service.findById(id);
    }
}
