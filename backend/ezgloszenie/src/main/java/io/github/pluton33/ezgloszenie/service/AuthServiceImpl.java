package io.github.pluton33.ezgloszenie.service;

import io.github.pluton33.ezgloszenie.data.*;
import io.github.pluton33.ezgloszenie.repository.UsersRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserMapper userMapper;
    private final UsersRepository repository;
    private final PasswordEncoder encoder;

    public AuthServiceImpl(UserMapper userMapper, UsersRepository repository, PasswordEncoder encoder) {
        this.userMapper = userMapper;
        this.repository = repository;
        this.encoder = encoder;
    }

    @Override
    public User addUser(RegisterUserRequest userRequest) {
        UserEntity userEntity = userMapper.toEntity(userRequest);
        userEntity.setCreatedAt(LocalDate.now());
        userEntity.setRole(UserRole.USER);
        //hashowanie hasła
        String encodedPassword = encoder.encode(userRequest.passwordHash());
        userEntity.setPasswordHash(encodedPassword);

        if (repository.existsByEmail(userRequest.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email jest zajęty");
        }
        System.out.println(userEntity);
        //dodawanie do bazy poprzez repository
        UserEntity addedUser = repository.save(userEntity);
        return userMapper.toDto(addedUser);
    }
}
