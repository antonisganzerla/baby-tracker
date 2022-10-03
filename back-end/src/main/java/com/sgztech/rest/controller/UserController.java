package com.sgztech.rest.controller;

import com.sgztech.rest.dto.*;
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

    @PostMapping("/password/forgot")
    public String forgot(@RequestBody @Valid ForgotPasswordDTO dto) {
        return service.forgotPassword(dto);
    }

    @PostMapping("password/code")
    public String code(@RequestBody @Valid ForgotPasswordCodeDTO dto) {
        return service.verificationCode(dto);
    }

    @PostMapping("password/reset")
    public String reset(@RequestBody @Valid ResetPasswordDTO dto) {
        return service.resetPassword(dto);
    }
}
