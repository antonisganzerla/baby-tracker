package com.sgztech.rest.controller;

import com.sgztech.rest.dto.BabyDTO;
import com.sgztech.service.BabyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/baby")
public class BabyController {

    @Autowired
    private BabyService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BabyDTO save(@RequestBody @Valid BabyDTO baby) {
        return service.save(baby);
    }

    @GetMapping("{id}")
    public BabyDTO getById(@PathVariable Integer id) {
        return service.getById(id);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Integer id,
                       @RequestBody @Valid BabyDTO baby) {
        service.update(id, baby);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }

    @GetMapping
    public List<BabyDTO> find(BabyDTO babyFilter) {
        return service.find(babyFilter);
    }
}