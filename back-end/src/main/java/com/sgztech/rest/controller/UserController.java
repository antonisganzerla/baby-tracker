package com.sgztech.rest.controller;

import com.sgztech.rest.dto.CreateUserDTO;
import com.sgztech.rest.dto.CredentialsDTO;
import com.sgztech.rest.dto.UserDTO;
import com.sgztech.rest.dto.UserTokenDTO;
import com.sgztech.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO save(@RequestBody @Valid CreateUserDTO dto) {
        return service.save(dto);
    }

    @GetMapping("{id}")
    public UserDTO getById(@PathVariable Integer id) {
        return service.getById(id);
    }

    @PostMapping("/auth")
    public UserTokenDTO auth(@RequestBody @Valid CredentialsDTO dto) {
        return service.auth(dto);
    }
}
