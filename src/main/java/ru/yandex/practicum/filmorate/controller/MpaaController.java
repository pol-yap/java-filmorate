package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.SimpleEntity;
import ru.yandex.practicum.filmorate.service.MpaaService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
public class MpaaController {
    @Autowired
    private MpaaService service;

    @GetMapping
    public List<SimpleEntity> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public SimpleEntity findById(@PathVariable int id) {
        return service.findById(id);
    }
}
