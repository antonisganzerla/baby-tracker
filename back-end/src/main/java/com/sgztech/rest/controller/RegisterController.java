package com.sgztech.rest.controller;

import com.sgztech.rest.dto.RegisterDTO;
import com.sgztech.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/register")
public class RegisterController {

    @Autowired
    private RegisterService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterDTO save(@RequestBody @Valid RegisterDTO dto) {
        return service.save(dto);
    }

    @GetMapping("{id}")
    public RegisterDTO getById(@PathVariable Integer id) {
        return service.getById(id);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Integer id,
                       @RequestBody @Valid RegisterDTO dto) {
        service.update(id, dto);
    }

    @DeleteMapping("{id}")
    //@ResponseStatus(HttpStatus.NO_CONTENT)
    public Integer delete(@PathVariable Integer id) {
        service.delete(id);
        return id;
    }

    @GetMapping
    public List<RegisterDTO> find(RegisterDTO filter) {
        return service.find(filter);
    }
}

