package com.sgztech.rest.controller;

import com.sgztech.domain.entity.UserProfile;
import com.sgztech.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user-profile")
public class UserProfileController {

    @Autowired
    private UserProfileService service;

    @GetMapping("{id}")
    public UserProfile getById(@PathVariable Integer id) {
        return service.getById(id);
    }

    @GetMapping
    public List<UserProfile> find(UserProfile filter) {
        return service.find(filter);
    }
}
