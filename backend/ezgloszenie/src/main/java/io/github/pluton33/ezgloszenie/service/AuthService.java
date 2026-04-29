package io.github.pluton33.ezgloszenie.service;

import io.github.pluton33.ezgloszenie.data.RegisterUserRequest;
import io.github.pluton33.ezgloszenie.data.User;

public interface AuthService {
    public User addUser(RegisterUserRequest userRequest);
}
