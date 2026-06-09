package io.github.pluton33.ezgloszenie.service;

import io.github.pluton33.ezgloszenie.data.User;
import io.github.pluton33.ezgloszenie.data.UserEntity;
import io.github.pluton33.ezgloszenie.repository.UsersRepository;

public interface UserService {
    public UserEntity getUserByLogin(String login);
    public User getUserProfile(String login);
}
