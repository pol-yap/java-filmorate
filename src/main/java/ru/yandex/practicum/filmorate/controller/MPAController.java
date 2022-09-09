package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.MPAService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/mpa")
public class MPAController {
    @Autowired
    private MPAService service;

    @GetMapping
    public List<MPA> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public MPA findById(@PathVariable int id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MPA create(@Valid @RequestBody MPA mpa) {
        return service.create(mpa);
    }

    @PutMapping
    public MPA update(@Valid @RequestBody MPA mpa) {
        return service.update(mpa.getId(), mpa);
    }
}
