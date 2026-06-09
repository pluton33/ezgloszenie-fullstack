package io.github.pluton33.ezgloszenie.controller;

import io.github.pluton33.ezgloszenie.data.User;
import io.github.pluton33.ezgloszenie.service.UserService;
import io.github.pluton33.ezgloszenie.data.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserService service;

    @GetMapping("profile/{login}")
    public User getUserProfile(@PathVariable String login) {
        return service.getUserProfile(login);
    }
}
