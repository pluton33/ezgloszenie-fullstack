package io.github.pluton33.ezgloszenie.controller;

import io.github.pluton33.ezgloszenie.data.RegisterUserRequest;
import io.github.pluton33.ezgloszenie.data.User;
import io.github.pluton33.ezgloszenie.service.AuthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @GetMapping("/login")
    String login() {
        return "login";
    }

    @PostMapping("/register")
    public User register(@RequestBody RegisterUserRequest userRequest) {
        return service.addUser(userRequest);
    }
}
